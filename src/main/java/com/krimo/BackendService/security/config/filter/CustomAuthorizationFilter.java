package com.krimo.BackendService.security.config.filter;


import com.krimo.BackendService.security.UsernamePasswordAuthToken;
import com.krimo.BackendService.security.utils.JWTUtility;
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

    private final JWTUtility jwtUtility;
    private final UsernamePasswordAuthToken usernamePasswordAuthToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader(AUTHORIZATION);

        if (header != null) {

            String username = jwtUtility.decodedJWT(header).getSubject();
            String [] roles = jwtUtility.decodedJWT(header).getClaim("role").asArray(String.class);

            Collection<SimpleGrantedAuthority> authorities = new ArrayList();
            stream(roles).forEach( auth -> authorities.add(new SimpleGrantedAuthority(auth)));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken
                    .authenticationToken(username,null, authorities));

        }

        filterChain.doFilter(request, response);
    }

}
