package egov.searcher;

import org.egov.tracer.config.TracerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Configuration
@ComponentScan(basePackages = { "egov.searcher" , "egov.searcher.config", "egov.searcher.web.controllers"})
@Import({TracerConfiguration.class})
public class Main {
        public static void main(String[] args) throws Exception {
            SpringApplication.run(Main.class, args);
        }
}
