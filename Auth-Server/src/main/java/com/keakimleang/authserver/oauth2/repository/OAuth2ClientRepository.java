package com.keakimleang.authserver.oauth2.repository;

import com.keakimleang.authserver.oauth2.entity.OAuth2Client;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface OAuth2ClientRepository extends JpaRepository<OAuth2Client, Long> {

    List<OAuth2Client> findByRegisteredFalse();

    List<OAuth2Client> findAllByUserId(Long userId);
}
