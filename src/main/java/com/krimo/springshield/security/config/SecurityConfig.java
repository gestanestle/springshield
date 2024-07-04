package com.krimo.springshield.security.config;

import com.krimo.springshield.security.PasswordEncoder;
import com.krimo.springshield.security.EmailPasswordAuthToken;
import com.krimo.springshield.security.config.filter.CustomAuthenticationFilter;
import com.krimo.springshield.security.config.filter.CustomAuthorizationFilter;
import com.krimo.springshield.service.UDSImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final UDSImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final EmailPasswordAuthToken authToken;

    @Bean
    public SecurityFilterChain configure (HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager(
                httpSecurity.getSharedObject(AuthenticationConfiguration.class)), authToken);
        customAuthenticationFilter.setFilterProcessesUrl("/api/v3/auth/login");

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .authorizeRequests(auth -> {
                    auth.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    auth.antMatchers("/api/v3/auth/signup", "/api/v3/auth/login").permitAll();
                    auth.antMatchers("/api/v3/user/**").hasAnyAuthority("USER", "ADMIN");
                    auth.antMatchers("/api/v3/admin/**").hasAuthority("ADMIN");
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

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;

    }

}

