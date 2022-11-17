package com.ntlevinkerschberger.payloads;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostRequest {

    String title;
    String content;

}
