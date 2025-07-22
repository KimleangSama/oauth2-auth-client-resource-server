package com.keakimleang.authserver.config;

import com.keakimleang.authserver.oauth2.customizer.jwt.JwtCustomizer;
import com.keakimleang.authserver.oauth2.customizer.jwt.JwtCustomizerHandler;
import com.keakimleang.authserver.oauth2.customizer.jwt.impl.JwtCustomizerImpl;
import com.keakimleang.authserver.oauth2.customizer.token.claims.OAuth2TokenClaimsCustomizer;
import com.keakimleang.authserver.oauth2.customizer.token.claims.impl.OAuth2TokenClaimsCustomizerImpl;
import com.keakimleang.authserver.oauth2.repository.JpaRegisteredClientRepository;
import com.keakimleang.authserver.oauth2.repository.OAuth2AuthorizationConsentRepository;
import com.keakimleang.authserver.oauth2.repository.OAuth2AuthorizationRepository;
import com.keakimleang.authserver.oauth2.repository.OAuth2RegisteredClientRepository;
import com.keakimleang.authserver.oauth2.service.JpaOAuth2AuthorizationConsentService;
import com.keakimleang.authserver.oauth2.service.JpaOAuth2AuthorizationService;
import com.keakimleang.authserver.oauth2.service.OAuth2RegisteredClientService;
import com.keakimleang.authserver.utils.jose.Jwks;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2TokenExchangeAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class AuthorizationServerConfig {
    private final OAuth2RegisteredClientService oauth2RegisteredClientService;

    private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

    @Value("${oauth2.token.issuer}")
    private String tokenIssuer;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        Customizer<OAuth2AuthorizationServerConfigurer> authorizationServerConfigurerCustomizer = (authorizationServer) -> {
            AuthenticationConverter authenticationConverter = getOAuth2AuthenticationConverter();
            Customizer<OAuth2TokenEndpointConfigurer> tokenEndpointCustomizer = (tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(authenticationConverter);
            Customizer<OAuth2AuthorizationEndpointConfigurer> authorizationEndpointCustomizer =
                    authorizationEndpoint -> authorizationEndpoint
                            .errorResponseHandler(
                                    (request, response, exception) -> {
                                        log.info("Error during authorization: {}", exception.getMessage());
                                        request.setAttribute("errorMessage", exception.getMessage());
                                        String errorPage;
                                        if (exception.getMessage().contains("redirect_uri")) {
                                            errorPage = "/error/redirect-uri-mismatch";
                                        } else {
                                            errorPage = "/error/authorization-error";
                                        }
                                        request.getRequestDispatcher(errorPage).forward(request, response);
                                    }
                            )
                            .consentPage(CUSTOM_CONSENT_PAGE_URI);
            authorizationServer.tokenEndpoint(tokenEndpointCustomizer)
                    .authorizationEndpoint(authorizationEndpointCustomizer);
        };
        http
                .securityMatcher(endpointsMatcher)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .with(authorizationServerConfigurer, authorizationServerConfigurerCustomizer);
        Customizer<IdentityConfigurer> identityConfigurerCustomizer = Customizer.withDefaults();
        http.with(new IdentityConfigurer(), identityConfigurerCustomizer);
        Customizer<FormLoginConfigurer<HttpSecurity>> formLoginCustomizer = Customizer.withDefaults();
        return http.formLogin(formLoginCustomizer).build();
    }

    private AuthenticationConverter getOAuth2AuthenticationConverter() {
        List<AuthenticationConverter> delegates = List.of(
                new OAuth2AuthorizationCodeAuthenticationConverter(),
                new OAuth2TokenExchangeAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2ClientCredentialsAuthenticationConverter()
        );
        return new DelegatingAuthenticationConverter(delegates);
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(OAuth2RegisteredClientRepository oauth2RegisteredClientRepository) {
        RegisteredClientRepository registeredClientRepository =
                new JpaRegisteredClientRepository(oauth2RegisteredClientRepository);
        log.info("in registeredClientRepository");
        List<RegisteredClient> registeredClients = oauth2RegisteredClientService.getOAuth2RegisteredClient();
        registeredClients.forEach(registeredClientRepository::save);
        return registeredClientRepository;
    }

    @Bean
    public OAuth2AuthorizationService authorizationService(OAuth2AuthorizationRepository oauth2AuthorizationRepository, RegisteredClientRepository registeredClientRepository) {
        return new JpaOAuth2AuthorizationService(oauth2AuthorizationRepository, registeredClientRepository);
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(OAuth2AuthorizationConsentRepository oauth2AuthorizationConsentRepository, RegisteredClientRepository registeredClientRepository) {
        return new JpaOAuth2AuthorizationConsentService(oauth2AuthorizationConsentRepository, registeredClientRepository);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = Jwks.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer(tokenIssuer).build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> buildJwtCustomizer() {
        JwtCustomizerHandler jwtCustomizerHandler = JwtCustomizerHandler.getJwtCustomizerHandler();
        JwtCustomizer jwtCustomizer = new JwtCustomizerImpl(jwtCustomizerHandler);
        return jwtCustomizer::customizeToken;
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> buildOAuth2TokenClaimsCustomizer() {
        OAuth2TokenClaimsCustomizer oauth2TokenClaimsCustomizer = new OAuth2TokenClaimsCustomizerImpl();
        return oauth2TokenClaimsCustomizer::customizeTokenClaims;
    }
}
