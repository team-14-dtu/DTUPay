package dk.dtu.team14.fakes;

import messaging.Event;
import messaging.MessageQueue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class FakeMessageQueue implements MessageQueue {
    private final HashMap<String, List<Consumer<Event>>> handlers = new HashMap<>();

    @Override
    public void publish(Event message) {
        var handlersForEvent = handlers.get(message.getType());
        for (Consumer<Event> handler : handlersForEvent) {
            handler.accept(message);
        }
    }

    @Override
    public void addHandler(String eventType, Consumer<Event> handler) {
        if (handlers.containsKey(eventType)) {
            handlers.get(eventType).add(handler);
        } else {
            final var newList = new ArrayList<Consumer<Event>>();
            newList.add(handler);

            handlers.put(
                    eventType,
                    newList
            );
        }
    }
}
