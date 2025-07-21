package com.keakimleang.client.config.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;

public interface OAuth2Client {
    ClientRegistration getClientRegistration();
}
