package com.krimo.BackendService.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Configuration
public class JWTUtility {

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256("secret".getBytes());
    }

    public String createJwt(String username, String requestUrl, Collection<? extends GrantedAuthority> roles, int mills) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + mills))
                .withIssuer(requestUrl)
                .withClaim("role", roles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm());
    }

    public DecodedJWT decodedJWT(String header) {
        JWTVerifier verifier = JWT.require(algorithm()).build();
        return verifier.verify(header);
    }


}
