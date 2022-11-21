package com.ntlevinkerschberger.dataInitializing;

import com.ntlevinkerschberger.models.Post;
import com.ntlevinkerschberger.models.user.User;
import com.ntlevinkerschberger.repositories.PostRepository;
import com.ntlevinkerschberger.repositories.UserRepository;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Singleton
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationEventListener<ApplicationStartupEvent> {

    private final PostRepository postRepository;


    private final UserRepository userRepository;
    private final TransactionOperations<?> tx;

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        log.info("initializing sample Posts...");
        var posts = List.of(Post.builder().createdAt(LocalDateTime.now()).upVotes(5).downVotes(4).content("Testcontent").title("Getting started with Micronaut DATA").build(),
              Post.builder().createdAt(LocalDateTime.now().plusHours(4)).upVotes(4).downVotes(3).title("Exposing a Rest API with Micronaut").content("Test").build());
        tx.executeWrite(status -> {
            this.postRepository.deleteAll();
            this.postRepository.saveAll(posts);
            return null;
        });
        tx.executeRead(status -> {
            this.postRepository.findAll().forEach(p -> log.info("saved post: {}", p));
            return null;
        });
        log.info("data initialization is done...");

        log.info("initializing sample Users...");
        var users = List.of(
                User.builder().username("user1")
                        .password("password1").build(),
                User.builder().username("user2")
                        .password("password2").build(),
                User.builder().username("user3")
                        .password("password3").build()
        );
        tx.executeWrite(status -> {
            this.userRepository.deleteAll();
            this.userRepository.saveAll(users);
            return null;
        });
        tx.executeRead(status -> {
            this.userRepository.findAll().forEach(u -> log.info("saved post: {}", u));
            return null;
        });

        log.info("Saved Users");
    }
}
