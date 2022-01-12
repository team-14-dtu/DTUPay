package behaviourtests;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CompletableFuture;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import messaging.Event;
import messaging.MessageQueue;
import studentregistration.service.Student;
import studentregistration.service.StudentRegistrationService;

public class TokenManagementSteps {
	private MessageQueue q = mock(MessageQueue.class);
	private CompletableFuture<Student> registeredStudent = new CompletableFuture<>();

	public TokenManagementSteps() {
	}


}
