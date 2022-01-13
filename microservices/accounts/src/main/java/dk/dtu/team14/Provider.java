package dk.dtu.team14;

import io.quarkus.runtime.configuration.ProfileManager;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class Provider {
    @Produces
    @ApplicationScoped
    MessageQueue messageQueue() {
        return new RabbitMqQueue(QueueUtils.getQueueName(ProfileManager.getActiveProfile()));
    }
}
