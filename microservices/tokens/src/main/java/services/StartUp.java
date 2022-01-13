package services;

import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("Starting token management service");
		var mq = new RabbitMqQueue(QueueUtils.getQueueName());
		new TokenManagementService(mq);
	}
}
