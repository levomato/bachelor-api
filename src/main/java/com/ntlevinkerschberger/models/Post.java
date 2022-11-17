package com.ntlevinkerschberger.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="posts")
public class Post implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy = "uuid2")
    UUID id;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm")
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd'T'HH:mm")
    LocalDateTime updatedAt = LocalDateTime.now();

    String title;

    String content;

    @Builder.Default
    long upVotes = 0;
    @Builder.Default
    long downVotes = 0;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "post", fetch = FetchType.EAGER)
    @Builder.Default
    @JsonIgnore
    List<Comment> comments = new ArrayList<>();

}
