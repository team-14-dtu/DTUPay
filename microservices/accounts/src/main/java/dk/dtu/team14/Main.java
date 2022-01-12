package dk.dtu.team14;

import event.account.RequestRegisterUser;
import messaging.Event;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import rest.RegisterUser;
import sharedMisc.QueueUtils;

import java.util.function.Consumer;

public class Main {
    private final MessageQueue queue = new RabbitMqQueue("localhost");

    public static void main(String[] args) {
        Main main = new Main();
        main.start();

    }

    private void start() {
        queue.addHandler(RequestRegisterUser.topic, event -> System.out.println(event));
    }
}
