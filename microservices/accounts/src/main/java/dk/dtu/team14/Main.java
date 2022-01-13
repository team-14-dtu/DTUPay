package dk.dtu.team14;

import event.account.RequestRegisterUser;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;
import sharedMisc.QueueUtils;

import javax.inject.Inject;


@QuarkusMain
public class Main implements QuarkusApplication {

//    @Inject
//    MessageQueue queue;

    @Override
    public int run(String... args) throws Exception {
        System.out.println(System.getProperties());
//        queue.addHandler(RequestRegisterUser.topic, event -> System.out.println(event));
        return 0;
    }
}
