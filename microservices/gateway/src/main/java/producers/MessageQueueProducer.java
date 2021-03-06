package producers;
// @author : Petr
import event.account.RegisterUserReplied;
import event.account.RetireUserReplied;
import io.quarkus.runtime.Startup;
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

    @Startup // Quarkus is lazy by default
    @Produces
    @ApplicationScoped
    ReplyWaiter replyWaiter(MessageQueue queue) {
        return new ReplyWaiter(queue,
                RegisterUserReplied.topic,
                RetireUserReplied.topic
        );
    }
}
