package com.keakimleang.authserver.oauth2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Entity
@Table(name = "oauth2_authorization_consent")
@IdClass(OAuth2AuthorizationConsentEntity.AuthorizationConsentId.class)
public class OAuth2AuthorizationConsentEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "registered_client_id")
    private String registeredClientId;

    @Id
    @Column(name = "principal_name")
    private String principalName;

    @Column(length = 1000)
    private String authorities;

    public void setRegisteredClientId(String registeredClientId) {
        this.registeredClientId = registeredClientId;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    @Getter
    @Setter
    @ToString
    public static class AuthorizationConsentId implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        private String registeredClientId;
        private String principalName;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            AuthorizationConsentId that = (AuthorizationConsentId) o;
            return registeredClientId.equals(that.registeredClientId) && principalName.equals(that.principalName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registeredClientId, principalName);
        }
    }

}
