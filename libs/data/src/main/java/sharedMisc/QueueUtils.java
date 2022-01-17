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

    public static String getQueueName(String profile) {
        if (Objects.equals(profile, "dev")
        ) {
            return "localhost";
        } else if (Objects.equals(profile, "prod")) {
            return "rabbitMQ";
        } else {
            throw new IllegalArgumentException("Error in profile-name: needs dev or prod");
        }
    }
}
