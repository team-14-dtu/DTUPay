package services;

import event.account.RegisterUserReplied;
import event.account.RegisterUserRequested;
import event.account.RetireUserReplied;
import event.account.RetireUserRequested;
import messaging.Event;
import messaging.MessageQueue;
import rest.RegisterUser;
import rest.RetireUser;
import services.errors.DTUPayError;
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
        this.waiter = waiter;
    }

    public UUID registerUser(RegisterUser registerUser) throws DTUPayError {
        final UUID correlationId = UUID.randomUUID();
        if (registerUser.getCpr() == null) return UUID.randomUUID(); //TODO: Need a error message as return

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

        if (reply.isSuccess()) {
            return reply.getSuccessResponse().getCustomerId();
        } else {
            throw new DTUPayError(reply.getFailureResponse().getReason());
        }
    }

    public String retireUser(RetireUser retireUser) {
        var correlationId = UUID.randomUUID();
        waiter.registerWaiterForCorrelation(correlationId);

        queue.publish(new Event(RetireUserRequested.topic, new Object[]{
                new RetireUserRequested(
                        correlationId,
                        retireUser.getCpr()
                )}));

        var event = waiter.synchronouslyWaitForReply(
                correlationId
        );

        var reply = event.getArgument(0, RetireUserReplied.class);

        // Very nice!!!!
        return reply.getSuccessResponse().toString();
    }
}
