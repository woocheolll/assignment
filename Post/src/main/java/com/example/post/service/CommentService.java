package com.example.post.service;

import com.example.post.dto.CommentRequestDto;
import com.example.post.dto.CommentResponseDto;
import com.example.post.dto.PostRequestDto;
import com.example.post.dto.PostResponseDto;
import com.example.post.entity.Comment;
import com.example.post.entity.Post;
import com.example.post.entity.User;
import com.example.post.entity.UserRoleEnum;
import com.example.post.jwt.JwtUtil;
import com.example.post.repository.CommentRepository;
import com.example.post.repository.PostRepository;
import com.example.post.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void createComment(Long postId, CommentRequestDto commentRequestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);   // 헤더에서 토큰 값 가져오기
        Claims claims;

        if (token != null) {    // token 값이 있음
            if (jwtUtil.validateToken(token)) { // 유효한 token
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰");
            }

            // 토큰의 username db에 있는지 확인
            User user = userRepository.findByUsername(claims.getSubject())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자의 토큰"));
            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("게시글 ID가 유효하지 않습니다."));
            Comment comment = new Comment(commentRequestDto.getComment(), user, post);
            commentRepository.save(comment);
        }
    }
    public CommentResponseDto updateComment(Long postId,Long commentId, CommentRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 url"));
        if (token != null) {    // token 값이 있음
            if (jwtUtil.validateToken(token)) { // 유효한 token
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰");
            }

            // 토큰의 username db에 있는지 확인
            User user = userRepository.findByUsername(claims.getSubject())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자의 토큰"));

            if (user.getRole() == UserRoleEnum.ADMIN || user.getId().equals(post.getUser().getId())) {
                Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 Comment ID"));
                comment.updateComment(requestDto);
                return new CommentResponseDto(comment);

            } else {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
        }
        throw new IllegalArgumentException("토큰 없음");

    }

    public void deleteComment(Long postid,Long commentId,HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);   // 헤더에서 토큰 값 가져오기
        Claims claims;
        Post post = postRepository.findById(postid).orElseThrow(
                () -> new IllegalArgumentException("잘못된 url"));
        if (token != null) {    // token 값이 있음
            if (jwtUtil.validateToken(token)) { // 유효한 token
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("유효하지 않은 토큰");
            }

            // 토큰의 username db에 있는지 확인
            User user = userRepository.findByUsername(claims.getSubject())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자의 토큰"));
            if (user.getRole() == UserRoleEnum.ADMIN || user.getId().equals(post.getUser().getId())) {
                commentRepository.deleteById(commentId);
                return;
            } else {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
        }
        throw new IllegalArgumentException("토큰 없음");

    }

}
