package sharedMisc;

import java.util.Objects;

public class QueueUtils {
    public static String getQueueName() {
        if (Objects.equals(System.getProperty("vertxweb.environment"), "dev")
        ) {
            return "localhost";
        } else {
            return "rabbitMQ";
        }
    }
}
