package com.krimo.BackendService.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class UsernamePasswordAuthToken {

    public UsernamePasswordAuthenticationToken authenticationToken (String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    public UsernamePasswordAuthenticationToken authenticationToken (String username, String password, Collection<? extends GrantedAuthority> authorities) {
        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

}


