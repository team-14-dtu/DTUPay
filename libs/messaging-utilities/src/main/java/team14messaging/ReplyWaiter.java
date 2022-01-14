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
        System.out.println("reply waiter initialized");
        for (String topic : topics) {
            System.out.println("adding handler");
            queue.addHandler(topic, event -> {
                var result = event.getArgument(0, BaseEvent.class);
//                registrationResult.put(result.getCorrelationId(), event);
                synchronized (registrationResult) {
                    registrationResult.notifyAll();
                }
            });
        }
    }

    public <T extends BaseEvent> T synchronouslyWaitForReply(
            String correlationId,
            Class<T> eventClass
    ) {
        Event event = null;
        do {
            try {
                synchronized (registrationResult) {
                    registrationResult.wait();
                }
            } catch (Exception e) {
                System.err.println(e);
            }
//            event = registrationResult.get(correlationId);
        } while (event == null);

        return event.getArgument(0, eventClass);
    }
}
