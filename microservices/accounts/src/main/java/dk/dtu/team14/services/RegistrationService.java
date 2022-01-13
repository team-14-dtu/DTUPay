package dk.dtu.team14.services;

import dk.dtu.team14.adapters.db.Database;
import event.account.RequestRegisterUser;
import messaging.MessageQueue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegistrationService {

        private final MessageQueue queue;
        private final Database database;

        public RegistrationService(MessageQueue queue, Database database) {
                this.queue = queue;
                this.database = database;
        }

        public void handleIncomingMessages() {
                queue.addHandler(RequestRegisterUser.topic, event -> {
                        System.out.println(event);
                        database.retire("123");
                });
        }
}
