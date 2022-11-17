package com.ntlevinkerschberger.controllers;

import com.ntlevinkerschberger.models.Comment;
import com.ntlevinkerschberger.models.Post;
import com.ntlevinkerschberger.payloads.CreateCommentRequest;
import com.ntlevinkerschberger.payloads.CreatePostRequest;
import com.ntlevinkerschberger.repositories.CommentRepository;
import com.ntlevinkerschberger.repositories.PostRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.micronaut.http.HttpResponse.*;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/posts")
@RequiredArgsConstructor()
@Validated
public class PostController {
    @Inject
    private final PostRepository postRepository;
    @Inject
    private final CommentRepository commentRepository;

    @Get(uri = "/", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<List<Post>> getAllPosts() {
        var body = postRepository.findAll();
        return ok(body);
    }

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<Post> getPostById(@PathVariable UUID id) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent())
            return badRequest();
        else
            return ok(postItem.get());
    }

    @io.micronaut.http.annotation.Post
    public HttpResponse<?> createPost(@Body @Valid CreatePostRequest createPostRequest) {
        Post post = Post.builder().title(createPostRequest.getTitle()).content(createPostRequest.getContent()).createdAt(LocalDateTime.now()).build();
        postRepository.save(post);
        return created(HttpStatus.CREATED.getCode() + ": Post Saved successfully !");
    }

    @Patch("/updateContent/{id}")
    public HttpResponse<?> updatePostContent(@PathVariable UUID id, @Body String content) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent()) {
            return badRequest();
        } else {
            Post post = postItem.get();
            post.setUpdatedAt(LocalDateTime.now());
            post.setContent(content);
            postRepository.update(post);
            return ok(post);
        }
    }


    @Patch("/updateTitle/{id}")
    public HttpResponse<?> updatePostTitle(@PathVariable UUID id, @Body String title) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent()) {
            return badRequest();
        } else {
            Post post = postItem.get();
            post.setUpdatedAt(LocalDateTime.now());
            post.setTitle(title);
            postRepository.update(post);
            return ok(post);
        }
    }

    @Patch("/upvote/{id}")
    public HttpResponse<?> upVotePost(@PathVariable UUID id) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent()) {
            return badRequest();
        } else {
            Post post = postItem.get();
            post.setUpVotes(post.getUpVotes() + 1);
            postRepository.update(post);
            return ok(post);
        }
    }
    @Patch("/downvote/{id}")
    public HttpResponse<?> downVotePost(@PathVariable UUID id) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent()) {
            return badRequest();
        } else {
            Post post = postItem.get();
            post.setDownVotes(post.getDownVotes() + 1);
            postRepository.update(post);
            return ok(post);
        }
    }

    @Delete("/{id}")
    public HttpResponse<?> deletePost(@PathVariable UUID id) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent())
            return badRequest();

        Post post = postItem.get();
        postRepository.delete(post);
        return ok(HttpStatus.OK.getCode() + " Post deleted sucessfully");
    }

    @io.micronaut.http.annotation.Post("/addcomment/{id}")
    public HttpResponse<?> addNewCommentToEntity(@PathVariable UUID id, @Body @Valid CreateCommentRequest createCommentRequest) {
        Optional<Post> postItem = postRepository.findById(id);
        if (!postItem.isPresent())
            return badRequest();
        Comment newComment = Comment.builder().post(postItem.get()).description(createCommentRequest.getContent()).build();
        commentRepository.save(newComment);
        postItem.get().getComments().add(newComment);
        postRepository.update(postItem.get());
        return created(newComment);
    }
}
