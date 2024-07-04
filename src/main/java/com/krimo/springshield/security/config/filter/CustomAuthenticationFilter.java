package com.krimo.springshield.security.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krimo.springshield.security.EmailPasswordAuthToken;

import com.krimo.springshield.model.User;
import com.krimo.springshield.security.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final EmailPasswordAuthToken authToken;

    /**
     * This method is a custom implementation of a attemptAuthentication
     * method provided by Spring Security.
     *
     * @param request       from which to extract parameters and perform the authentication
     * @param response      the response, which may be needed if the implementation has to do a
     *                      redirect as part of a multi-stage authentication process (such as OpenID).
     * @return authentication      the authentication of credentials
     * @throws AuthenticationException  authentication exception
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        return authenticationManager.authenticate(authToken.authenticationToken(email, password));
    }

    /**
     * This method sets access and refresh tokens upon successful authentication.
     *
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt>
     * method.
     * @throws IOException
     * @throws ServletException
     */

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException {

        User user = (User) authResult.getPrincipal();

        Map<String, String> tokens = new HashMap<>();
        tokens.putIfAbsent(
                "accessToken",
                JWTUtils.createAccessToken(request.getRequestURL().toString(), user)) ;
        tokens.putIfAbsent(
                "refreshToken",
                JWTUtils.createRefreshToken(request.getRequestURL().toString(), user));

        OutputStream out = response.getOutputStream();
        response.setContentType(APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(out,
                Map.of(
                        "status", 200,
                        "message", "Authentication Successful",
                        "data", tokens
                ));
    }

}

