package com.keakimleang.authserver.oauth2.customizer.jwt.impl;

import com.keakimleang.authserver.oauth2.customizer.jwt.JwtCustomizerHandler;
import com.keakimleang.authserver.service.CustomUserDetails;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.util.CollectionUtils;

public class UsernamePasswordAuthenticationTokenJwtCustomizerHandler extends AbstractJwtCustomizerHandler {

    public UsernamePasswordAuthenticationTokenJwtCustomizerHandler(JwtCustomizerHandler jwtCustomizerHandler) {
        super(jwtCustomizerHandler);
    }

    @Override
    protected void customizeJwt(JwtEncodingContext jwtEncodingContext) {
        Authentication authentication = jwtEncodingContext.getPrincipal();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        Long userId = user.getId();
        Set<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Map<String, Object> userAttributes = new HashMap<>();
        userAttributes.put("userId", userId);

        Set<String> contextAuthorizedScopes = jwtEncodingContext.getAuthorizedScopes();

        JwtClaimsSet.Builder jwtClaimSetBuilder = jwtEncodingContext.getClaims();

        if (CollectionUtils.isEmpty(contextAuthorizedScopes)) {
            jwtClaimSetBuilder.claim(OAuth2ParameterNames.SCOPE, authorities);
        }

        jwtClaimSetBuilder.claims(claims ->
                claims.putAll(userAttributes)
        );

    }

    @Override
    protected boolean supportCustomizeContext(Authentication authentication) {
        return authentication instanceof UsernamePasswordAuthenticationToken;
    }
}
