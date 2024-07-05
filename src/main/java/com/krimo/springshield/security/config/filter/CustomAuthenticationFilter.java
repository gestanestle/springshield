package com.krimo.springshield.security.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.springshield.dto.ResponseObject;
import com.krimo.springshield.security.EmailPasswordAuthToken;

import com.krimo.springshield.model.User;
import com.krimo.springshield.security.utils.JWTUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.krimo.springshield.utils.Utils.tooManyRequests;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final EmailPasswordAuthToken authToken;
    private final Bucket bucket;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, EmailPasswordAuthToken authToken) {
        this.authenticationManager = authenticationManager;
        this.authToken = authToken;

        // 3 allowed logins every 15 minutes
        Bandwidth loginLimit = Bandwidth.classic(3, Refill.intervally(3, Duration.ofMinutes(15)));
        this.bucket = Bucket.builder()
                .addLimit(loginLimit)
                .build();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (!bucket.tryConsume(1)) tooManyRequests(response);

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return authenticationManager.authenticate(authToken.authenticationToken(email, password));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {

        User user = (User) authResult.getPrincipal();

        Map<String, String> tokens = new HashMap<>();
        tokens.putIfAbsent(
                "accessToken",
                JWTUtils.createAccessToken(request.getRequestURL().toString(), user));
        tokens.putIfAbsent(
                "refreshToken",
                JWTUtils.createRefreshToken(request.getRequestURL().toString(), user));

        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(out, new ResponseObject(200,  "Authentication successful", tokens));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        SecurityContextHolder.clearContext();
        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(401);
        new ObjectMapper().writeValue(out, new ResponseObject(401, "Invalid credentials", null));
    }

}