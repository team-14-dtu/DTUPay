package event;

import java.util.Objects;

public class QueueNames {
    public static String getQueueName() {
        if (Objects.equals(System.getProperty("vertxweb.environment"), "dev")) {
            return "localhost";
        } else {
            return "rabbitMQ";
        }
    }
}
