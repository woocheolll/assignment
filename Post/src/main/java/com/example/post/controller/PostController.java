package com.example.post.controller;



import com.example.post.dto.PostRequestDto;
import com.example.post.dto.PostResponseDto;
//import com.example.post.service.CommentService;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
//    private final CommentService commentService;


    @PostMapping("/create")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(postService.createPost(requestDto, request));
    }
    @GetMapping("/post/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @GetMapping("/posts")
    public List<PostResponseDto> getPostList() {
        return postService. getPostList();
    }

    @ResponseBody
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(postService.update(id, requestDto, request));
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest request) {
        postService.delete(id, request);
        return ResponseEntity.ok("success");
    }
}
