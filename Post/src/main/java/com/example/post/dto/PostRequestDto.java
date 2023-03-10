package com.example.post.dto;

import com.example.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter

public class PostRequestDto {
    private String title;
    private String content;

}
