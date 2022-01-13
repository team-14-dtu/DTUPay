package dk.dtu.team14;

import dk.dtu.team14.services.RegistrationService;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.configuration.ProfileManager;

import javax.inject.Inject;


@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    RegistrationService registrationService;

    @Override
    public int run(String... args)  {
        registrationService.handleIncomingMessages();
        return 0;
    }
}
