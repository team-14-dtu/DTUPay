package team14messaging;

import messaging.Event;
import messaging.MessageQueue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ReplyWaiter {

    private final static Map<String, CompletableFuture<Event>> registrationResult = Collections.synchronizedMap(new HashMap<>());
    private final MessageQueue queue;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public ReplyWaiter(MessageQueue queue, String... topics) {
        this.queue = queue;
        for (String topic : topics) {
            queue.addHandler(topic, event -> {
                var result = event.getArgument(0, BaseEvent.class);
                System.out.println(registrationResult.get(result.getCorrelationId()));
                registrationResult.get(result.getCorrelationId()).complete(event);
            });
        }
    }

    public void registerWaiterForCorrelation(String correlationId) {
        var future = new CompletableFuture<Event>();
        registrationResult.put(correlationId, future);
    }

    public Event synchronouslyWaitForReply(
            String correlationId
    ) {
        return registrationResult.get(correlationId).join();
    }
}
