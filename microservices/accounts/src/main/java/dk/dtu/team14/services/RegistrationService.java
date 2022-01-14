package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import event.account.*;
import messaging.Event;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;

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
        queue.addHandler(RequestRegisterUser.topic, this::handleRegisterRequest);
//        queue.addHandler(RequestRetireUser.topic, this::handleRetireRequest);
    }

    private void publishErrorDuringRegistration(String cpr, String message) {
        queue.publish(new Event(
                ReplyRegisterUser.topic,
                new Object[]{new ReplyRegisterUser(
                        cpr,
                        cpr,
                        null,
                        new ReplyRegisterUserFailure(message)
                )}
        ));
    }


    public void handleRegisterRequest(Event event) {
        final var createUserRequest = event.getArgument(0, RequestRegisterUser.class);
        System.out.println("Handling register request cpr - " + createUserRequest.getCpr());

        if (!bank.doesBankAccountExist(createUserRequest.getBankAccountId())) {
            publishErrorDuringRegistration(createUserRequest.getCpr(), "User was not created, bank account doesn't exist");
            return;
        }

        var newUser = database.save(
                createUserRequest.getName(),
                createUserRequest.getCpr(),
                createUserRequest.getBankAccountId()
        );

        if (newUser != null) {
            var replyEvent = new ReplyRegisterUser(
                    newUser.cpr,
                    newUser.cpr,
                    new ReplyRegisterUserSuccess(
                            newUser.name,
                            newUser.bankAccountId,
                            newUser.cpr,
                            newUser.id
                    ),
                    null
            );

            queue.publish(new Event(
                    ReplyRegisterUser.topic,
                    new Object[]{replyEvent}
            ));
        } else {
            publishErrorDuringRegistration(createUserRequest.getCpr(), "User could not be registered");
        }
    }

    public void handleRetireRequest(Event event) {
        System.out.println("Handling retire request");
        final var retireUserRequest = event.getArgument(0, RequestRetireUser.class);
        final var success = database.retire(retireUserRequest.getCustomerId());

        queue.publish(new Event(
                ReplyRetireUser.topic,
                new Object[]{new ReplyRetireUser(
                        retireUserRequest.getCustomerId(),
                        success
                )}
        ));
    }
}
