package com.ntlevinkerschberger.models.user;

import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @Builder.Default
    private String role = "VIEW";
}

