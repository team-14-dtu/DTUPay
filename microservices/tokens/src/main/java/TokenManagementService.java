import messaging.Event;
import messaging.MessageQueue;

public class TokenManagementService {
    MessageQueue queue;

    public TokenManagementService(MessageQueue q) {
        this.queue = q;
        this.queue.addHandler("StudentRegistrationRequested", this::handleStudentRegistrationRequested);
    }

    public void handleStudentRegistrationRequested(Event ev) {
        /*var s = ev.getArgument(0, Student.class);
        s.setId("123");
        Event event = new Event("StudentIdAssigned", new Object[] { s });
        queue.publish(event);*/
    }
}
