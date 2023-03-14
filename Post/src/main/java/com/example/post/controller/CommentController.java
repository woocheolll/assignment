package com.example.post.controller;

import com.example.post.dto.CommentRequestDto;
import com.example.post.dto.CommentResponseDto;
import com.example.post.dto.PostResponseDto;
import com.example.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@ResponseBody
@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;


    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<String> createComment(@PathVariable Long postId,
                                                @RequestBody CommentRequestDto requestDto,
                                                HttpServletRequest request) {
        commentService.createComment(postId, requestDto, request);
        return ResponseEntity.ok("댓글 성공");
    }
    @PutMapping("/post/{postId}/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody CommentRequestDto requestDto,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(commentService.updateComment(postId,commentId, requestDto, request));
    }
    @DeleteMapping("/post/{postId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId,
                                                @PathVariable Long commentId,
                                                HttpServletRequest request) {
        commentService.deleteComment(postId,commentId, request);
        return ResponseEntity.ok("success");
    }


}
