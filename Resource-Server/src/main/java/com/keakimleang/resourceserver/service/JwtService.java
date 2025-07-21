package com.keakimleang.resourceserver.service;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.Assert;

public interface JwtService {
    String USER_ID_CLAIM = "userId";

    static <T> T getClaim(Jwt jwt, String claim) {
        Assert.notNull(jwt, "jwt cannot be null");
        Assert.hasText(claim, "claim cannot be null or empty");
        T value = null;
        if (jwt.hasClaim(claim)) {
            value = jwt.getClaim(claim);
        }
        return value;
    }

    static Long getUserId(Jwt jwt) {
        return getClaim(jwt, USER_ID_CLAIM);
    }
}
