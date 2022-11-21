package com.ntlevinkerschberger;

import com.ntlevinkerschberger.models.user.User;
import com.ntlevinkerschberger.models.user.UserDto;
import com.ntlevinkerschberger.models.user.UserMapper;
import com.ntlevinkerschberger.services.UserService;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.Optional;


@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {
    @Inject
    UserService userService;

    @Inject
    UserMapper userMapper;
    @Override
    public Publisher<AuthenticationResponse> authenticate(HttpRequest<?> httpRequest, AuthenticationRequest<?, ?> authenticationRequest) {

        final String username = authenticationRequest.getIdentity().toString();
        final String password = authenticationRequest.getSecret().toString();

        Optional<User> existingUser = userService.findUser(username).map(userMapper::toEntity);

        return Flux.create(emitter -> {
            if(username.equals(existingUser.get().getUsername()) && password.equals(existingUser.get().getPassword())) {
                emitter.next(AuthenticationResponse.success((String) authenticationRequest.getIdentity()));
                emitter.complete();
            }
            else {
             emitter.error(AuthenticationResponse.exception());
            }}, FluxSink.OverflowStrategy.ERROR);

    }



}
