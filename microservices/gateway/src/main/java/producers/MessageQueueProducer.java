package producers;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class MessageQueueProducer {

    @Produces
    @ApplicationScoped
    MessageQueue messageQueue() {
        return new RabbitMqQueue(QueueUtils.getQueueName());
    }
}
