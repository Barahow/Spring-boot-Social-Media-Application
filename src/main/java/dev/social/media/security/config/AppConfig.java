package dev.social.media.security.config;

import dev.social.media.security.Errors.CustomErrorResponse;
import dev.social.media.security.Errors.GlobalExceptionHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ProblemDetail;

@Configuration
@AllArgsConstructor
public class AppConfig {

    @Bean
    public CustomErrorResponse getProblemDetail() {
        return new CustomErrorResponse();
    }


}



