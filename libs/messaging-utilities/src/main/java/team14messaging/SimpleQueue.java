package team14messaging;
// @author : Petr
import messaging.Event;
import messaging.MessageQueue;

import java.util.function.Consumer;

public class SimpleQueue {

    private final MessageQueue queue;

    public SimpleQueue(MessageQueue queue) {
        this.queue = queue;
    }

    public void publish(String topic, BaseEvent event) {
        queue.publish(new Event(topic, new Object[]{event}));
    }

    public void addHandler(String eventType, Consumer<Event> handler) {
        queue.addHandler(eventType, handler);
    }
}
