package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import event.account.*;
import messaging.Event;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class RegistrationService {

    private final MessageQueue queue;
    private final Database database;
    private final Bank bank;

    ExecutorService executorOfQueueListeners = Executors.newFixedThreadPool(2);

    public RegistrationService(MessageQueue queue, Database database, Bank bank) {
        this.queue = queue;
        this.database = database;
        this.bank = bank;
    }

    public void handleIncomingMessages() throws ExecutionException, InterruptedException {
        var task1 = executorOfQueueListeners.submit(() -> {
            queue.addHandler(RequestRegisterUser.topic, this::handleRegisterRequest);
        });

        var task2 = executorOfQueueListeners.submit(() -> {
            queue.addHandler(RequestRetireUser.topic, this::handleRetireRequest);
        });

        // Blocks the thread indefinitely
        task1.get();
        task2.get();
    }

    private void publishErrorDuringRegistration(String cpr, String message) {
        queue.publish(new Event(
                ReplyRegisterUser.topic,
                new Object[]{new ReplyRegisterUser(
                        cpr,
                        null,
                        new ReplyRegisterUserFailure(message)
                )}
        ));
    }


    public void handleRegisterRequest(Event event) {

        final var createUserRequest = event.getArgument(0, RequestRegisterUser.class);
        if (bank.checkBankAccountExist(createUserRequest.getBankAccountId())) {
            publishErrorDuringRegistration(createUserRequest.getCpr(), "User was not created, bank account doesn't exist");
            return;
        }

        var newUser = database.save(
                createUserRequest.getName(),
                createUserRequest.getCpr(),
                createUserRequest.getBankAccountId()
        );

        if (newUser != null) {
            queue.publish(new Event(
                    ReplyRegisterUser.topic,
                    new Object[]{new ReplyRegisterUser(
                            newUser.cpr, new ReplyRegisterUserSuccess(
                            newUser.name,
                            newUser.bankAccountId,
                            newUser.cpr,
                            newUser.id
                    ),
                            null
                    )}
            ));
        } else {
            publishErrorDuringRegistration(createUserRequest.getCpr(), "User could not be registered");
        }
    }

    public void handleRetireRequest(Event event) {
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
