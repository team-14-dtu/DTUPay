package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import event.BaseReplyEvent;
import event.account.*;
import messaging.Event;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class RegistrationService {

    private final MessageQueue queue;
    private final Database database;
    private final Bank bank;

    public RegistrationService(MessageQueue queue, Database database, Bank bank) {
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

    private void handleBankAccountIdFromCustomerId(Event event) {
        var request =
                event.getArgument(0, BankAccountIdFromCustomerIdRequested.class);
        System.out.println("Handling event1 in registration service: " + request.getCorrelationId());
        queue.publish(
                new Event(
                        BankAccountIdFromCustomerIdReplied.topic,
                        new Object[]{
                                new BankAccountIdFromCustomerIdReplied(
                                        request.getCorrelationId(),
                                        new BankAccountIdFromCustomerIdReplied.Success(
                                                request.getCustomerId(),
                                                database.findById(request.getCustomerId()).bankAccountId
                                        )
                                )
                        }
                )
        );
    }

    private void handleRequestBankAccountIdFromMerchantId(Event event) {
        BankAccountIdFromMerchantIdRequested request =
                event.getArgument(0, BankAccountIdFromMerchantIdRequested.class);

        System.out.println("Handling event2 in registration service: " + request.getCorrelationId());
        queue.publish(
                new Event(
                        BankAccountIdFromMerchantIdReplied.topic,
                        new Object[]{
                                new BankAccountIdFromMerchantIdReplied(
                                        request.getCorrelationId(),
                                        database.findById(request.getMerchantId()).bankAccountId
                                )
                        }
                )
        );
    }

    private void publishErrorDuringRegistration(String cpr, UUID correlationIn, String message) {
        queue.publish(new Event(
                RegisterUserReplied.topic,
                new Object[]{new RegisterUserReplied(
                        correlationIn,
                        new BaseReplyEvent.SimpleFailure(message)
                )}
        ));
    }


    public void handleRegisterRequest(Event event) {
        final var createUserRequest = event.getArgument(0, RegisterUserRequested.class);
        System.out.println("Handling register request cpr - " + createUserRequest.getCpr());

        if (!bank.doesBankAccountExist(createUserRequest.getBankAccountId())) {
            publishErrorDuringRegistration(
                    createUserRequest.getCpr(),
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
                    new RegisterUserReplied.RegisterUserRepliedSuccess(
                            newUser.name,
                            newUser.bankAccountId,
                            newUser.cpr,
                            newUser.id
                    )
            );

            queue.publish(new Event(
                    RegisterUserReplied.topic,
                    new Object[]{replyEvent}
            ));
        } else {
            publishErrorDuringRegistration(createUserRequest.getCpr(),
                    createUserRequest.getCorrelationId(),
                    "User could not be registered");
        }
    }

    public void handleRetireRequest(Event event) {
        System.out.println("Handling retire request");
        final var retireUserRequest = event.getArgument(0, RetireUserRequested.class);
        final var success = database.retire(retireUserRequest.getCpr());

        queue.publish(new Event(
                RetireUserReplied.topic,
                new Object[]{new RetireUserReplied(
                        retireUserRequest.getCorrelationId(),
                        success
                )}
        ));
    }
}
