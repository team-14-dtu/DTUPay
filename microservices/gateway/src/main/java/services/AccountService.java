package services;

import event.account.ReplyRegisterUser;
import event.account.RequestRegisterUser;
import messaging.Event;
import messaging.MessageQueue;
import rest.RegisterUser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AccountService {

    private final MessageQueue queue;
    private final Map<String, ReplyRegisterUser> registrationResults =
            Collections.synchronizedMap(new HashMap<>());

    @Inject
    public AccountService(MessageQueue queue) {
        this.queue = queue;
        queue.addHandler(ReplyRegisterUser.topic, this::handleUserRegistered);
    }

    private synchronized void handleUserRegistered(Event event) {
        var registrationResult = event.getArgument(0, ReplyRegisterUser.class);
        registrationResults.put(registrationResult.getCpr(), registrationResult);
        notifyAll();
    }

    public synchronized String registerUser(RegisterUser registerUser) {
        var event = new RequestRegisterUser(
                registerUser.getName(),
                registerUser.getBankAccountId(),
                registerUser.getCpr(),
                registerUser.getIsMerchant()
        );

        queue.publish(new Event(RequestRegisterUser.topic, new Object[]{event}));

        while (!registrationResults.containsKey(registerUser.getCpr())) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        final var result = registrationResults.get(registerUser.getCpr());
        registrationResults.remove(registerUser.getCpr());

        if (result.getSuccessResponse() != null) {
            return result.getSuccessResponse().getCustomerId();
        } else {
            return result.getFailResponse().getMessage();
        }
    }
}
