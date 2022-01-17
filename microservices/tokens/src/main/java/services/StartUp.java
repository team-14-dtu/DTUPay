package services;

import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp(args[0]);
	}

	private void startUp(String profile) throws Exception {
		System.out.println("Starting token management service");
		System.out.println("Provided profile: "+profile);
		var mq = new RabbitMqQueue(QueueUtils.getQueueName(profile));
		new TokenManagementService(mq);
	}
}
