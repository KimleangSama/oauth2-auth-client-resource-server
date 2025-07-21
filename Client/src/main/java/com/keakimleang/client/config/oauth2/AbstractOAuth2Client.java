package com.keakimleang.client.config.oauth2;

import org.springframework.core.env.Environment;

public abstract class AbstractOAuth2Client implements OAuth2Client {
    protected String oauth2AuthorizationUri;
    protected String oauth2TokenUri;

    public AbstractOAuth2Client(Environment env) {
        this.oauth2AuthorizationUri = env.getProperty("oauth2.authorization.uri");
        this.oauth2TokenUri = env.getProperty("oauth2.token.uri");
    }

}
