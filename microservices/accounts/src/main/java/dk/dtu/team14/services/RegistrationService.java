package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import event.account.RequestRegisterUser;
import event.account.RequestRetireUser;
import messaging.Event;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletableFuture;
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
        var task1= executorOfQueueListeners.submit(() -> {
            queue.addHandler(RequestRegisterUser.topic, this::handleRegistrationRequest);
        });

        var task2 = executorOfQueueListeners.submit(() -> {
            queue.addHandler(RequestRetireUser.topic, this::handleRetireRequest);
        });

        // Blocks the thread indefinitely
        task1.get();
        task2.get();
    }

    public void handleRetireRequest(Event event) {
        System.out.println("retire");
    }

    public void handleRegistrationRequest(Event event) {
        System.out.println("asdf");
    }
}
