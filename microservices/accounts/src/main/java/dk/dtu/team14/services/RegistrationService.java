package dk.dtu.team14.services;

import dk.dtu.team14.adapters.bank.Bank;
import dk.dtu.team14.adapters.db.Database;
import event.account.RequestRegisterUser;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationService {

        private final MessageQueue queue;
        private final Database database;
        private final Bank bank;

        public RegistrationService(MessageQueue queue, Database database, Bank bank) {
                this.queue = queue;
                this.database = database;
                this.bank = bank;
        }

        public void handleIncomingMessages() {
                queue.addHandler(RequestRegisterUser.topic, event -> {
                        System.out.println(event);
                        database.retire("123");
                });
        }
}
