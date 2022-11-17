package com.ntlevinkerschberger.payloads;

import com.ntlevinkerschberger.models.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {
    Post post;
    String content;
}
