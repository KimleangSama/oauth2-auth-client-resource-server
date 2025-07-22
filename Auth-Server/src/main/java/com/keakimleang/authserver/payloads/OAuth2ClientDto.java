package com.keakimleang.authserver.payloads;

import com.keakimleang.authserver.oauth2.entity.OAuth2Client;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@Setter
@ToString
public class OAuth2ClientDto {
    private Long id;
    private String clientId;
    private Instant clientIdIssuedAt;
    private String clientName;
    private String clientSecret;
    private Instant clientSecretExpiresAt;
    private String clientAuthenticationMethods;
    private String authorizationGrantTypes;
    private String[] redirectUris;
    private String[] scopes;
    private boolean registered;

    public static OAuth2ClientDto from(OAuth2Client oauth2Client) {
        OAuth2ClientDto dto = new OAuth2ClientDto();
        dto.setId(oauth2Client.getId());
        dto.setClientId(oauth2Client.getClientId());
        dto.setClientIdIssuedAt(oauth2Client.getClientIdIssuedAt());
        dto.setClientName(oauth2Client.getClientName());
        dto.setClientSecret(oauth2Client.getClientSecret());
        dto.setClientSecretExpiresAt(oauth2Client.getClientSecretExpiresAt());
        dto.setClientAuthenticationMethods(oauth2Client.getClientAuthenticationMethods());
        dto.setAuthorizationGrantTypes(oauth2Client.getAuthorizationGrantTypes());
        dto.setRedirectUris(StringUtils.commaDelimitedListToStringArray(oauth2Client.getRedirectUris()));
        dto.setScopes(StringUtils.commaDelimitedListToStringArray(oauth2Client.getScopes()));
        dto.setRegistered(oauth2Client.isRegistered());
        return dto;
    }
}
