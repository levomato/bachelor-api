package com.ntlevinkerschberger.controllers;

import com.ntlevinkerschberger.models.user.UserDto;
import com.ntlevinkerschberger.services.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import rx.Single;


import java.util.Optional;

@Controller("/signup")
@Secured(SecurityRule.IS_ANONYMOUS)
public class SignUpController {

    @Inject
    UserService userService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post
    public HttpResponse<?> registerUser(UserDto userDto) {
        Optional<UserDto> existingUser = userService.findUser(userDto.getUsername());
        if(existingUser.isPresent())
            return HttpResponse.badRequest().body("User already exists!");

        return HttpResponse.created(userService.createUser(userDto));
    }
}
