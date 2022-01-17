package team14messaging;

import messaging.Event;
import messaging.MessageQueue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ReplyWaiter {

    private final static Map<String, CompletableFuture> registrationResult = Collections.synchronizedMap(new HashMap<>());
    private final MessageQueue queue;

    public ReplyWaiter(MessageQueue queue, String... topics) {
        this.queue = queue;
        for (String topic : topics) {
            queue.addHandler(topic, event -> {
                var result = event.getArgument(0, BaseEvent.class);
                if (registrationResult.containsKey(result.getCorrelationId())) {
                    System.out.println(registrationResult.get(result.getCorrelationId()));
                    registrationResult.get(result.getCorrelationId()).complete(event);
                }
            });
        }
    }

    public Event synchronouslyWaitForReply(
            String correlationId
    ) {
        var future = new CompletableFuture<Event>();
        registrationResult.put(correlationId, future);
        return future.join();
    }
}
