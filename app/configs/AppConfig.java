package configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({
                "controllers", "services",
})
// Have spring scan the controllers and services directories.
public class AppConfig {

}
