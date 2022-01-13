package dk.dtu.team14;

import dk.dtu.team14.services.RegistrationService;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

import javax.inject.Inject;
import java.util.concurrent.ExecutionException;


@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    RegistrationService registrationService;

    @Override
    public int run(String... args) throws ExecutionException, InterruptedException {
        registrationService.handleIncomingMessages();
        return 0;
    }
}
