package com.krimo.BackendService.security.config;

import com.krimo.BackendService.security.PasswordEncoder;
import com.krimo.BackendService.security.UsernamePasswordAuthToken;
import com.krimo.BackendService.security.config.filter.CustomAuthenticationFilter;
import com.krimo.BackendService.security.config.filter.CustomAuthorizationFilter;
import com.krimo.BackendService.service.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Value("${frontend-url}")
    private String allowedOrigin;

    private final ServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsernamePasswordAuthToken authToken;

    @Bean
    public SecurityFilterChain configure (HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(
                httpSecurity.getSharedObject(AuthenticationConfiguration.class)), authToken);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v2/login");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests(auth -> {
                    auth.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.antMatchers("/api/v2/auth/**", "/api/v2/login/**").permitAll();
                    auth.antMatchers("/api/v2/user/**").hasAnyAuthority("USER", "ADMIN");
                    auth.antMatchers("/api/v2/admin/**").hasAuthority("ADMIN");
                    auth.anyRequest().authenticated();
                })
                .addFilter(customAuthenticationFilter).authorizeRequests().and()
                .addFilterBefore(new CustomAuthorizationFilter(authToken), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling();

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    public AuthenticationManager authManager () {
        return authManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;

    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}

