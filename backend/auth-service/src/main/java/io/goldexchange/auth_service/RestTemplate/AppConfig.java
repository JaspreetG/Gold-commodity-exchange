package io.goldexchange.auth_service.RestTemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for application beans.
 */
@Configuration
public class AppConfig {

    /**
     * Creates a RestTemplate bean for making HTTP requests.
     *
     * @return A new RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

