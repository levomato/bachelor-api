package com.ntlevinkerschberger.models.user;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;

    @Builder.Default
    private String role = "VIEW";
}

