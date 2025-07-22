package com.keakimleang.authserver.oauth2.entity;

import com.keakimleang.authserver.entities.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "client_id", unique = true, nullable = false)
	private String clientId;
	
	@Column(name = "client_id_issued_at")
	private Instant clientIdIssuedAt;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "client_secret")
	private String clientSecret;
	
	@Column(name = "client_secret_expires_at")
	private Instant clientSecretExpiresAt;

	@Column(name = "authentication_method")
	private String clientAuthenticationMethods;

	@Column(name = "authorization_grant_type")
	private String authorizationGrantTypes;

	@Column(name = "redirect_uris")
	private String redirectUris;

	private String scopes;

	private boolean registered;

	@ManyToOne(cascade = CascadeType.ALL, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@OneToOne(mappedBy = "oauth2Client", cascade = CascadeType.ALL, optional = false)
	private OAuth2ClientTokenSetting tokenSetting;

	@OneToOne(mappedBy = "oauth2Client", cascade = CascadeType.ALL)
	private OAuth2ClientSetting clientSetting;
}
