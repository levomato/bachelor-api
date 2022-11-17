package com.ntlevinkerschberger.repositories;

import com.ntlevinkerschberger.models.Comment;
import com.ntlevinkerschberger.models.Post;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPost(Post post);
}
