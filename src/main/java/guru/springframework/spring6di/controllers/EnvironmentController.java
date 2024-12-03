package guru.springframework.spring6di.controllers;

import guru.springframework.spring6di.services.EnvironmentService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

@Controller
@Lazy
public class EnvironmentController {
    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public String getEnvironment() {
        return "You are in " + environmentService.getEnv() + " Environment";
    }
}
