package com.keakimleang.authserver.entities;

import jakarta.persistence.Embeddable;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;

@Getter
@Setter
@ToString
@Embeddable
public class UserAuthority implements GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;

    private String authority;

    public UserAuthority() {
    }

    public UserAuthority(String authority) {
        Assert.hasText(authority, "A granted authority textual representation is required");
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SimpleGrantedAuthority) {
            return this.authority.equals(((SimpleGrantedAuthority) obj).getAuthority());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }
}
