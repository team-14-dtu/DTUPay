package producers;

import event.account.ReplyRegisterUser;
import event.account.ReplyRetireUser;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class MessageQueueProducer {

    @Produces
    @ApplicationScoped
    MessageQueue messageQueue() {
        return new RabbitMqQueue(QueueUtils.getQueueName());
    }

    @Produces
    @ApplicationScoped
    ReplyWaiter replyWaiter(MessageQueue queue) {
        return new ReplyWaiter(queue,
                ReplyRegisterUser.topic,
                ReplyRetireUser.topic
        );
    }
}
