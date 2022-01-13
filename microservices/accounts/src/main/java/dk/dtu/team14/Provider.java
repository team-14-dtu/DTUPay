package dk.dtu.team14;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.bank.implementations.SoapBankAdapter;
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

    @Produces
    @ApplicationScoped
    Bank bank() {
        return new SoapBankAdapter();
    }
}
