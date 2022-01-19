package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import dk.dtu.team14.entities.User;
import event.BaseReplyEvent;
import event.account.*;
import messaging.Event;
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
    }

    public void handleBankAccountIdFromCustomerId(Event event) {
        var request =
                event.getArgument(0, BankAccountIdFromCustomerIdRequested.class);

        if (!request.isSuccess()) {
            publishUserNotFoundError(request.getCorrelationId());
            return;
        }

        User customer = database.findById(request.getSuccessResponse().getCustomerId());

        if (customer == null) {
            publishUserNotFoundError(request.getCorrelationId());
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

    public void handleRequestBankAccountIdFromMerchantId(Event event) {
        BankAccountIdFromMerchantIdRequested request =
                event.getArgument(0, BankAccountIdFromMerchantIdRequested.class);

        User merchant = database.findById(request.getMerchantId());

        if (merchant == null) {
            publishUserNotFoundError(request.getCorrelationId());
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

    public void handleRegisterRequest(Event event) {
        final var createUserRequest = event.getArgument(0, RegisterUserRequested.class);
        System.out.println("Handling register request cpr - " + createUserRequest.getCpr());

        if (!bank.doesBankAccountExist(createUserRequest.getBankAccountId())) {
            publishSimpleFailure(
                    createUserRequest.getCorrelationId(),
                    "User was not created, bank account doesn't exist");
            return;
        }

        var newUser = database.save(
                createUserRequest.getName(),
                createUserRequest.getCpr(),
                createUserRequest.getBankAccountId()
        );

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
                    createUserRequest.getCorrelationId(),
                    "User could not be registered");
        }
    }

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

    // -------- Error responses ----------
    private void publishUserNotFoundError(UUID correlationId) {
        publishSimpleFailure(correlationId, "User not found");
    }

    private void publishSimpleFailure(UUID correlationIn, String message) {
        queue.publish(
                RegisterUserReplied.topic,
                new RegisterUserReplied(
                        correlationIn,
                        new BaseReplyEvent.SimpleFailure(message)
                )
        );
    }

}
