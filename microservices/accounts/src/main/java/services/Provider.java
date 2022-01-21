package services;
// @author : Emmanuel
import io.quarkus.runtime.configuration.ProfileManager;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import services.adapters.bank.Bank;
import services.adapters.bank.implementations.SoapBankAdapter;
import sharedMisc.QueueUtils;
import team14messaging.SimpleQueue;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

public class Provider {
    @Produces
    @ApplicationScoped
    MessageQueue messageQueue() {
        return new RabbitMqQueue(QueueUtils.getQueueName(ProfileManager.getActiveProfile()));
    }

    @Produces
    @ApplicationScoped
    SimpleQueue simpleQueue(MessageQueue queue) {
        return new SimpleQueue(queue);
    }

    @Produces
    @ApplicationScoped
    Bank bank() {
        return new SoapBankAdapter();
    }
}
