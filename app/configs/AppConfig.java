package configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

@Configuration
@ComponentScan({
    "controllers", "services", "forms"
})
// Have spring scan the controllers and services directories.
public class AppConfig {

}
