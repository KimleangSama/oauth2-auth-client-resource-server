package com.keakimleang.authserver.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "oauth2_authorization")
public class OAuth2AuthorizationEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "registered_client_id")
	private String registeredClientId;

	@Column(name = "principal_name")
	private String principalName;

	@Column(name = "authorization_grant_type")
	private String authorizationGrantType;

	@Column(name = "authorized_scopes", length = 1000)
	private String authorizedScopes;

	@Column(length = 4000)
	private String attributes;

	@Column(length = 500)
	private String state;

	@Column(name = "authorization_code_value", length = 4000)
	private String authorizationCodeValue;

	@Column(name = "authorization_code_issued_at")
	private Instant authorizationCodeIssuedAt;

	@Column(name = "authorization_code_expires_at")
	private Instant authorizationCodeExpiresAt;

	@Column(name = "authorization_code_metadata")
	private String authorizationCodeMetadata;

	@Column(name = "access_token_value", length = 4000)
	private String accessTokenValue;

	@Column(name = "access_token_issued_at")
	private Instant accessTokenIssuedAt;

	@Column(name = "access_token_expires_at")
	private Instant accessTokenExpiresAt;

	@Column(name = "access_token_metadata", length = 2000)
	private String accessTokenMetadata;

	@Column(name = "access_token_type")
	private String accessTokenType;

	@Column(name = "access_token_scopes", length = 1000)
	private String accessTokenScopes;

	@Column(name = "refresh_token_value", length = 4000)
	private String refreshTokenValue;

	@Column(name = "refresh_token_issued_at")
	private Instant refreshTokenIssuedAt;

	@Column(name = "refresh_token_expires_at")
	private Instant refreshTokenExpiresAt;

	@Column(name = "refresh_token_metadata", length = 2000)
	private String refreshTokenMetadata;

	@Column(name = "oidc_id_token_value", length = 4000)
	private String oidcIdTokenValue;

	@Column(name = "oidc_id_token_issued_at")
	private Instant oidcIdTokenIssuedAt;

	@Column(name = "oidc_id_token_expires_at")
	private Instant oidcIdTokenExpiresAt;

	@Column(name = "oidc_id_token_metadata", length = 2000)
	private String oidcIdTokenMetadata;

}
