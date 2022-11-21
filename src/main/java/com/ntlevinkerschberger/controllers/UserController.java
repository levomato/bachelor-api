package com.ntlevinkerschberger.controllers;

import com.ntlevinkerschberger.models.user.User;
import com.ntlevinkerschberger.models.user.UserMapper;
import com.ntlevinkerschberger.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;

import java.util.Optional;

import static io.micronaut.http.HttpResponse.badRequest;
import static io.micronaut.http.HttpResponse.ok;

@Controller("/users")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class UserController {

    @Inject
    UserService userService;
    @Inject
    UserMapper mapper;

    @Get("/{username}")
    public HttpResponse<?> GetUserByUsername(@PathVariable String username) {

        Optional<User> userItem = userService.findUser(username).map(mapper::toEntity);

        if(!userItem.isPresent())
            return badRequest();
        return ok(userItem.get());

    }
}
