package com.keakimleang.authserver.oauth2.repository;

import com.keakimleang.authserver.oauth2.entity.OAuth2RegisteredClient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2RegisteredClientRepository extends JpaRepository<OAuth2RegisteredClient, String> {
    Optional<OAuth2RegisteredClient> findByClientId(String clientId);
}
