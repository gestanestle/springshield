package com.krimo.springshield.security.config.filter;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.krimo.springshield.security.EmailPasswordAuthToken;
import com.krimo.springshield.security.utils.JWTUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final EmailPasswordAuthToken authToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring("Bearer ".length());

            DecodedJWT decoded = JWTUtils.decodeToken(token);

            String email = decoded.getSubject();
            String[] roles = decoded.getClaim("roles").asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            stream(roles).forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth)));

            SecurityContextHolder.getContext().setAuthentication(authToken
                    .authenticationToken(email, null, authorities));
        }

        filterChain.doFilter(request, response);
    }

}
