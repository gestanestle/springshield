package com.krimo.springshield.security.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.krimo.springshield.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtils {

    private static final String SIGN = "secret-sign";
    private static final String ROLES_CLAIM = "roles";
    private static final String GRANT_CLAIM = "grant-type";
    private static final int ACCESS_MILLIS = 900000; // Access tokens last for 15 minutes
    private static final int REFRESH_MILLIS = 1209600000; // Refresh tokens last for 14 days
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SIGN.getBytes());

    private JWTUtils() {}

    public static String createAccessToken(String requestUrl, User user) {
        return JWT.create()
                .withSubject(String.valueOf(user.getEmail()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_MILLIS))
                .withIssuer(requestUrl)
                .withClaim(ROLES_CLAIM, getRoles(user.getAuthorities()))
                .withClaim(GRANT_CLAIM, "access")
                .sign(ALGORITHM);
    }

    public static String createRefreshToken(String requestUrl, User user) {
        return JWT.create()
                .withSubject(String.valueOf(user.getEmail()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_MILLIS))
                .withIssuer(requestUrl)
                .withClaim(ROLES_CLAIM, getRoles(user.getAuthorities()))
                .withClaim(GRANT_CLAIM, "refresh")
                .sign(ALGORITHM);
    }

    public static DecodedJWT decodeToken(String token) {
        JWTVerifier verifier = JWT.require(ALGORITHM).build();
        return verifier.verify(token);
    }

    public static String getGrantType(String token) {
        return decodeToken(token).getClaim(GRANT_CLAIM).asString();
    }

    private static List<String> getRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }

}
