package ch.nonam.worldcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@ComponentScan
@EnableAutoConfiguration
public class NonamWorldcatApp extends SpringBootServletInitializer {

    public static void main(final String[] args) {
        SpringApplication.run(NonamWorldcatApp.class, args);
    }

    @Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(NonamWorldcatApp.class);
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	return new RestTemplate();
    }
}
