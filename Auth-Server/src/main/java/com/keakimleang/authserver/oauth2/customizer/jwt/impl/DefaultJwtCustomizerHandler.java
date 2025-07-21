package com.keakimleang.authserver.oauth2.customizer.jwt.impl;

import com.keakimleang.authserver.oauth2.customizer.jwt.JwtCustomizerHandler;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

public class DefaultJwtCustomizerHandler implements JwtCustomizerHandler {
    @Override
    public void customize(JwtEncodingContext jwtEncodingContext) {
    }
}
