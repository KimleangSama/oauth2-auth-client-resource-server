package com.keakimleang.authserver.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
@Table(name = "oauth2_client_setting")
public class OAuth2ClientSetting implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "require_authorization_consent")
    private boolean requireAuthorizationConsent;

    @OneToOne
    @JoinColumn(name = "client_id", referencedColumnName = "client_id", nullable = false)
    private OAuth2Client oauth2Client;
}
