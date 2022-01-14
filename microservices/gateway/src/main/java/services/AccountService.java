package services;

import event.account.ReplyRegisterUser;
import event.account.RequestRegisterUser;
import messaging.Event;
import messaging.MessageQueue;
import rest.RegisterUser;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class AccountService {

    private final MessageQueue queue;
    private final ReplyWaiter waiter;

    @Inject
    public AccountService(MessageQueue queue, ReplyWaiter waiter) {
        this.queue = queue;
        System.out.println("asdf");
        this.waiter = waiter;
    }

    public String registerUser(RegisterUser registerUser) {
        System.out.println("registering user on " + Thread.currentThread().getName());
        if (registerUser.getCpr() == null) return "Cpr can't be null";

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        queue.publish(new Event(RequestRegisterUser.topic, new Object[]{
                new RequestRegisterUser(
                        registerUser.getCpr(),
                        registerUser.getName(),
                        registerUser.getBankAccountId(),
                        registerUser.getCpr(),
                        registerUser.getIsMerchant()
                )}));

        var event = waiter.synchronouslyWaitForReply(
                registerUser.getCpr()
        );

        var reply = event.getArgument(0, ReplyRegisterUser.class);

        System.out.println("Replying to cpr - " + registerUser.getCpr());
        if (reply.getSuccessResponse() != null) {
            return reply.getSuccessResponse().getCustomerId();
        } else {
            return reply.getFailResponse().getMessage();
        }
    }
}
