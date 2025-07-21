package com.keakimleang.authserver.oauth2.service;

import com.keakimleang.authserver.oauth2.entity.OAuth2ClientTokenSetting;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OAuth2TokenSettingsService {
    public TokenSettings getTokenSettings(OAuth2ClientTokenSetting clientTokenSetting) {
        final int accessTokenTime = clientTokenSetting.getAccessTokenTime();
        final String accessTokenTimeUnit = clientTokenSetting.getAccessTokenTimeUnit();
        final int refreshTokenTime = clientTokenSetting.getRefreshTokenTime();
        final String refreshTokenTimeUnit = clientTokenSetting.getRefreshTokenTimeUnit();

        Duration accessTokenDuration = setTokenTime(accessTokenTimeUnit, accessTokenTime, 5);
        Duration refreshTokenDuration = setTokenTime(refreshTokenTimeUnit, refreshTokenTime, 60);

        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder().accessTokenTimeToLive(accessTokenDuration)
                .refreshTokenTimeToLive(refreshTokenDuration);
        return tokenSettingsBuilder.build();
    }

    private Duration setTokenTime(String tokenTimeUnit, long tokenTime, long durationInMinutes) {
        Duration duration = Duration.ofMinutes(durationInMinutes);
        if (StringUtils.hasText(tokenTimeUnit)) {
            duration = switch (tokenTimeUnit.toUpperCase()) {
                case "M", "MINUTE", "MINUTES" -> Duration.ofMinutes(tokenTime);
                case "H", "HOUR", "HOURS" -> Duration.ofHours(tokenTime);
                case "D", "DAY", "DAYS" -> Duration.ofDays(tokenTime);
                case "W", "WEEK", "WEEKS" -> Duration.of(tokenTime, ChronoUnit.WEEKS);
                default -> duration;
            };
        }
        return duration;
    }

}
