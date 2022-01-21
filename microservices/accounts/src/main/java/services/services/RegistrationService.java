package services.services;

import event.BaseReplyEvent;
import event.account.*;
import messaging.Event;
import services.adapters.bank.Bank;
import services.adapters.db.Database;
import services.entities.User;
import team14messaging.SimpleQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class RegistrationService {

    private final SimpleQueue queue;
    private final Database database;
    private final Bank bank;

    public RegistrationService(SimpleQueue queue, Database database, Bank bank) {
        this.queue = queue;
        this.database = database;
        this.bank = bank;
    }
    // @author : Emmanuel
    public void handleIncomingMessages() {
        queue.addHandler(
                BankAccountIdFromMerchantIdRequested.topic,
                this::handleRequestBankAccountIdFromMerchantId
        );

        queue.addHandler(
                BankAccountIdFromCustomerIdRequested.topic,
                this::handleBankAccountIdFromCustomerId
        );
        queue.addHandler(
                RegisterUserRequested.topic,
                this::handleRegisterRequest
        );

        queue.addHandler(
                RetireUserRequested.topic,
                this::handleRetireRequest
        );

        queue.addHandler(
                UserExistsRequested.topic,
                this::handleUserExistsRequest
        );

    }
    // @author : Petr
    public void handleBankAccountIdFromCustomerId(Event event) {
        var request =
                event.getArgument(0, BankAccountIdFromCustomerIdRequested.class);

        if (!request.isSuccess()) {
            publishSimpleFailure(BankAccountIdFromCustomerIdReplied.topic, request.getCorrelationId(), request.getFailureResponse().getReason());
            return;
        }

        User customer = database.findById(request.getSuccessResponse().getCustomerId());

        if (customer == null) {
            publishSimpleFailure(BankAccountIdFromCustomerIdReplied.topic, request.getCorrelationId(), "User not found");
            return;
        }

        queue.publish(
                BankAccountIdFromCustomerIdReplied.topic,
                new BankAccountIdFromCustomerIdReplied(
                        request.getCorrelationId(),
                        new BankAccountIdFromCustomerIdReplied.Success(
                                request.getSuccessResponse().getCustomerId(),
                                customer.bankAccountId,
                                customer.name

                        )
                )
        );
    }
    // @author : Petr
    public void handleRequestBankAccountIdFromMerchantId(Event event) {
        BankAccountIdFromMerchantIdRequested request =
                event.getArgument(0, BankAccountIdFromMerchantIdRequested.class);

        User merchant = database.findById(request.getMerchantId());

        if (merchant == null) {
            publishSimpleFailure(BankAccountIdFromMerchantIdReplied.topic, request.getCorrelationId(), "User not found");
            return;
        }

        queue.publish(
                BankAccountIdFromMerchantIdReplied.topic,
                new BankAccountIdFromMerchantIdReplied(
                        request.getCorrelationId(),
                        new BankAccountIdFromMerchantIdReplied.Success(
                                merchant.bankAccountId,
                                merchant.name
                        )
                )
        );
    }
    // @author : Petr
    public void handleRegisterRequest(Event event) {
        final var genericErrorMessage = "User could not be registered";

        final var createUserRequest = event.getArgument(0, RegisterUserRequested.class);
        System.out.println("Handling register request cpr - " + createUserRequest.getCpr());

        if (!bank.doesBankAccountExist(createUserRequest.getBankAccountId())) {
            publishSimpleFailure(
                    RegisterUserReplied.topic,
                    createUserRequest.getCorrelationId(),
                    "User was not created, bank account doesn't exist");
            return;
        }

        User newUser;
        try {
            newUser = database.save(
                    createUserRequest.getName(),
                    createUserRequest.getCpr(),
                    createUserRequest.getBankAccountId()
            );
        } catch (Database.DatabaseError e) {
            publishSimpleFailure(
                    RegisterUserReplied.topic,
                    createUserRequest.getCorrelationId(),
                    genericErrorMessage
            );
            return;
        }

        if (newUser != null) {
            var replyEvent = new RegisterUserReplied(
                    createUserRequest.getCorrelationId(),
                    new RegisterUserReplied.Success(
                            newUser.name,
                            newUser.bankAccountId,
                            newUser.cpr,
                            newUser.id
                    )
            );

            queue.publish(
                    RegisterUserReplied.topic,
                    replyEvent
            );
        } else {
            publishSimpleFailure(
                    RegisterUserReplied.topic,
                    createUserRequest.getCorrelationId(),
                    genericErrorMessage);
        }
    }
    // @author : Emmanuel
    public void handleRetireRequest(Event event) {
        System.out.println("Handling retire request");
        final var retireUserRequest = event.getArgument(0, RetireUserRequested.class);
        final var success = database.removeByCpr(retireUserRequest.getCpr());

        RetireUserReplied reply;
        if (success) {
            reply = new RetireUserReplied(
                    retireUserRequest.getCorrelationId(),
                    new RetireUserReplied.Success()
            );
        } else {
            reply = new RetireUserReplied(
                    retireUserRequest.getCorrelationId(),
                    new BaseReplyEvent.SimpleFailure("User was not registered")
            );
        }

        queue.publish(
                RetireUserReplied.topic,
                reply
        );
    }
    // @author : David
    public void handleUserExistsRequest(Event event) {
        var request = event.getArgument(0, UserExistsRequested.class);
        if (database.findById(request.getUserId()) == null) {
            publishSimpleFailure(UserExistsReplied.topic, request.getCorrelationId(), "User does not exist");
        } else {
            queue.publish(
                    UserExistsReplied.topic,
                    new UserExistsReplied(
                            request.getCorrelationId(),
                            new UserExistsReplied.Success()
                    )
            );
        }
    }
    // @author : Petr
    // -------- Error responses ----------
    private void publishSimpleFailure(String topic, UUID correlationId, String message) {
        queue.publish(
                topic,
                new RegisterUserReplied(
                        correlationId,
                        new BaseReplyEvent.SimpleFailure(message)
                )
        );
    }

}
