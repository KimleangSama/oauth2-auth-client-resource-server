package com.keakimleang.authserver.config.handlers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserRepositoryHandler implements Consumer<OAuth2User> {
    private final OAuth2UserCacheRepository oAuth2UserCacheRepository = new OAuth2UserCacheRepository();

    @Override
    public void accept(OAuth2User user) {
        // Capture user in a local data store on first authentication
        if (this.oAuth2UserCacheRepository.findByName(user.getName()) == null) {
            System.out.println("Saving first-time user: name=" + user.getName() + ", claims=" + user.getAttributes() + ", authorities=" + user.getAuthorities());
            this.oAuth2UserCacheRepository.save(user);
        }
    }

    static class OAuth2UserCacheRepository {
        private final Map<String, OAuth2User> userCache = new ConcurrentHashMap<>();
        public OAuth2User findByName(String name) {
            return this.userCache.get(name);
        }
        public void save(OAuth2User oauth2User) {
            this.userCache.put(oauth2User.getName(), oauth2User);
        }
    }
}
