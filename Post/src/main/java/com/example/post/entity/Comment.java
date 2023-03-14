package com.example.post.entity;

import com.example.post.dto.CommentRequestDto;
import com.example.post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(String comment, User user, Post post){
        this.comment = comment;
        this.user = user;
        this.post = post;
    }

    public void updateComment(CommentRequestDto requestDto){
        this.comment = requestDto.getComment();
    }


}
