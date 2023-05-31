package dev.social.media.security.security;

import dev.social.media.security.config.PasswordEncoderConfig;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {


    private final UserDetailsService userDetailsService;

    //private UserAuthenticationService userAuthenticationService;


    private PasswordEncoderConfig passwordEncoderConfig;

    private final AuthenticationConfiguration authenticationConfiguration;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-uri}")
    String introspectionUri;

    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-id}")
    String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaque.introspection-client-secret}")
    String clientSecret;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/api/v1/login/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(GET,"/api/v1/user/**").hasAuthority("USER");

        http.authorizeHttpRequests().anyRequest().authenticated()
                .and().oauth2ResourceServer(oauth-> oauth.opaqueToken(token-> token.introspectionUri(this.introspectionUri).introspectionClientCredentials(this.clientId,this.clientSecret)));

        http.oauth2Login();

        return http.build();

    }


    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderConfig.passwordEncoder());
    }

}
