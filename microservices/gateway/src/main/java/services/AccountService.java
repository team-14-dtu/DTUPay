package services;

import event.account.RegisterUserReplied;
import event.account.RegisterUserRequested;
import messaging.Event;
import messaging.MessageQueue;
import rest.RegisterUser;
import team14messaging.ReplyWaiter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.UUID;

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
        final String correlationId = UUID.randomUUID().toString();
        if (registerUser.getCpr() == null) return "Cpr can't be null";

        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(RegisterUserRequested.topic, new Object[]{
                new RegisterUserRequested(
                        correlationId,
                        registerUser.getName(),
                        registerUser.getBankAccountId(),
                        registerUser.getCpr(),
                        registerUser.getIsMerchant()
                )}));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, RegisterUserReplied.class);

        if (reply.getSuccessResponse() != null) {
            return reply.getSuccessResponse().getCustomerId();
        } else {
            return reply.getFailResponse().getMessage();
        }
    }
}
