package services;
// @author : Petr
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import services.services.RegistrationService;

import javax.inject.Inject;


@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    RegistrationService registrationService;

    @Override
    public int run(String... args) throws InterruptedException {
        registrationService.handleIncomingMessages();
        synchronized (this) {
            this.wait();
        }
        return 0;
    }
}
