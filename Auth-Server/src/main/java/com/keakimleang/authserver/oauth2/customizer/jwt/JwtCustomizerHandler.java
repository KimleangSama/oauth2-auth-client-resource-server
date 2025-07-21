package com.keakimleang.authserver.oauth2.customizer.jwt;

import com.keakimleang.authserver.oauth2.customizer.jwt.impl.DefaultJwtCustomizerHandler;
import com.keakimleang.authserver.oauth2.customizer.jwt.impl.OAuth2AuthenticationTokenJwtCustomizerHandler;
import com.keakimleang.authserver.oauth2.customizer.jwt.impl.UsernamePasswordAuthenticationTokenJwtCustomizerHandler;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;

public interface JwtCustomizerHandler {
    void customize(JwtEncodingContext jwtEncodingContext);

    static JwtCustomizerHandler getJwtCustomizerHandler() {
        JwtCustomizerHandler defaultJwtCustomizerHandler = new DefaultJwtCustomizerHandler();
        JwtCustomizerHandler oauth2AuthenticationTokenJwtCustomizerHandler = new OAuth2AuthenticationTokenJwtCustomizerHandler(defaultJwtCustomizerHandler);
        return new UsernamePasswordAuthenticationTokenJwtCustomizerHandler(oauth2AuthenticationTokenJwtCustomizerHandler);
    }
}
