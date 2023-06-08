package dev.social.media.security.security;

import dev.social.media.security.config.PasswordEncoderConfig;
import dev.social.media.security.filter.JwtAuthenticationFilter;
import dev.social.media.security.filter.JwtAuthorizationFilter;
import dev.social.media.security.service.JwtTokenProvider;
import dev.social.media.security.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig {


    private final UserDetailsService userDetailsService;


    private PasswordEncoderConfig passwordEncoderConfig;

    private JwtTokenProvider jwtTokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;

    private final UserService userService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        AbstractAuthenticationProcessingFilter filter = new JwtAuthenticationFilter(authenticationManager(),jwtTokenProvider,userService);
        filter.setFilterProcessesUrl("/api/v1/login");


        http.csrf().disable();
          http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers("/api/v1/login/**", "/api/token/refresh/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(GET,"/api/v1/user/**").hasAuthority("USER");
        http.authorizeHttpRequests().requestMatchers(POST, "api/v1/user/**").hasAuthority("ADMIN");
        http.authorizeHttpRequests().requestMatchers(GET, "/api/v1/post/**",  "/api/v1/user/**").hasAnyAuthority("ADMIN");
        http.authorizeHttpRequests().requestMatchers(DELETE, "/api/v1/user/**","/api/v1/post/**").hasAuthority("USER");
        http.authorizeHttpRequests().requestMatchers(POST, "api/v1/post/**", "api/v1/user/**").hasAuthority("ADMIN");
        http.authorizeHttpRequests().requestMatchers(PUT, "/api/v1/post/**","/api/v1/user/**").hasAuthority("USER");
        http.authorizeHttpRequests().requestMatchers(PUT,  "/api/v1/post/**", "api/v1/user/**").hasAuthority("ADMIN");
        http.authorizeHttpRequests().requestMatchers(DELETE, "/api/v1/post/**", "/api/v1/user/**").hasAuthority("SUPER_ADMIN");

        http.authorizeHttpRequests().anyRequest().authenticated();

        http.addFilter(filter);

        http.addFilterBefore(new JwtAuthorizationFilter(jwtTokenProvider,userDetailsService), UsernamePasswordAuthenticationFilter.class);

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
