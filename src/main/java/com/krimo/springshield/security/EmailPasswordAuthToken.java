package com.krimo.springshield.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class EmailPasswordAuthToken {

    public UsernamePasswordAuthenticationToken authenticationToken (String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

    public UsernamePasswordAuthenticationToken authenticationToken (String email, String password, Collection<? extends GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

}


