package com.example.post.dto;

import lombok.Getter;
import lombok.Setter;
import org.intellij.lang.annotations.Pattern;

@Setter
@Getter
public class LoginRequestDto {

    private String username;

    private String password;
}