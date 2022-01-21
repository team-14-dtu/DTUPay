package services;

import messaging.implementations.RabbitMqQueue;
import services.db.implementations.StupidSimpleInMemoryDB;
import sharedMisc.QueueUtils;

public class StartUp {
    public static void main(String[] args) throws Exception {
        new StartUp().startUp(args[0]);
    }

    private void startUp(String profile) throws Exception {
        System.out.println("Starting token management service");
        System.out.println("Provided profile: " + profile);
        RabbitMqQueue mq = new RabbitMqQueue(QueueUtils.getQueueName(profile));
        StupidSimpleInMemoryDB db = new StupidSimpleInMemoryDB();
        new TokenManagementService(mq, db);
    }
}
