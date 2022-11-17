package com.ntlevinkerschberger;

import com.ntlevinkerschberger.models.RefreshToken;
import com.ntlevinkerschberger.repositories.RefreshTokenRepository;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;

import static io.micronaut.security.errors.IssuingAnAccessTokenErrorCode.INVALID_GRANT;

@Singleton
@AllArgsConstructor
public class CustomRefreshTokenPersistence implements RefreshTokenPersistence {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void persistToken(RefreshTokenGeneratedEvent event) {
        if(event!= null && event.getRefreshToken() != null && event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            String payload = event.getRefreshToken();
            refreshTokenRepository.save(event.getAuthentication().getName(), payload, false);
        }
    }

    @Override
    public Publisher<Authentication> getAuthentication(String refreshToken) {
        return Flux.create(emitter -> {
            Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenOpt.isPresent()) {
                RefreshToken token = tokenOpt.get();
                if (token.getRevoked()) {
                    emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token revoked", null));
                } else {
                    emitter.next(Authentication.build(token.getUsername()));
                    emitter.complete();
                }
            } else {
                emitter.error(new OauthErrorResponseException(INVALID_GRANT, "refresh token not found", null));
            }
        }, FluxSink.OverflowStrategy.ERROR);
    }
}
