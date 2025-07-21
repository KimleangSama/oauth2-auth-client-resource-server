package com.keakimleang.authserver.config;

import com.keakimleang.authserver.config.handlers.CustomIdentityAuthenticationEntryPoint;
import com.keakimleang.authserver.config.handlers.CustomIdentityAuthenticationSuccessHandler;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

@Slf4j
public final class IdentityConfigurer extends AbstractHttpConfigurer<IdentityConfigurer, HttpSecurity> {
    private Consumer<OAuth2User> oauth2UserHandler;

    public IdentityConfigurer oauth2UserHandler(Consumer<OAuth2User> oauth2UserHandler) {
        Assert.notNull(oauth2UserHandler, "oauth2UserHandler cannot be null");
        this.oauth2UserHandler = oauth2UserHandler;
        return this;
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        log.info("Initializing identity configuration");
        ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);
        ClientRegistrationRepository clientRegistrationRepository = applicationContext.getBean(ClientRegistrationRepository.class);
        String loginPageUrl = "/login";
        CustomIdentityAuthenticationEntryPoint authenticationEntryPoint =
                new CustomIdentityAuthenticationEntryPoint(loginPageUrl, clientRegistrationRepository);

        CustomIdentityAuthenticationSuccessHandler authenticationSuccessHandler = new CustomIdentityAuthenticationSuccessHandler();
        if (this.oauth2UserHandler != null) {
            authenticationSuccessHandler.setOAuth2UserHandler(this.oauth2UserHandler);
        }

        http
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint))
                .oauth2Login(oauth2Login ->
                        oauth2Login.successHandler(authenticationSuccessHandler));
    }

}
