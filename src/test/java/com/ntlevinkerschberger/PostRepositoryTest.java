package com.ntlevinkerschberger;

import com.ntlevinkerschberger.repositories.PostRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;



@MicronautTest(application = Application.class, startApplication = false)
public class PostRepositoryTest {
    @Inject
    PostRepository postRepository;



}
