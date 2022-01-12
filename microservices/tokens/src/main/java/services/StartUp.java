package services;

import messaging.implementations.RabbitMqQueue;

import static sharedMisc.QueueUtils.getQueueName;

public class StartUp {
	public static void main(String[] args) throws Exception {
		new StartUp().startUp();
	}

	private void startUp() throws Exception {
		System.out.println("Starting token management service");
		var mq = new RabbitMqQueue(getQueueName());
		new TokenManagementService(mq);
	}
}
