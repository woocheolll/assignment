package com.example.post.service;


import com.example.post.dto.PostRequestDto;
import com.example.post.dto.PostResponseDto;
import com.example.post.entity.Post;
import com.example.post.entity.User;
import com.example.post.entity.UserRoleEnum;
import com.example.post.jwt.JwtUtil;
import com.example.post.repository.PostRepository;
import com.example.post.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    ;


    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, HttpServletRequest request) {
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

            Post post = new Post(requestDto, user);
            postRepository.save(post);
            return new PostResponseDto(post);
        }
        throw new IllegalArgumentException("토큰 없음");
    }

    public PostResponseDto getPost(Long id) {
        Post post = checkPost(id);
        return new PostResponseDto(post);
    }


    public List<PostResponseDto> getPostList() {
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
        List<Post> postList = postRepository.findAll();
        for (Post post : postList) {
            postResponseDtoList.add(new PostResponseDto(post));
        }
        return postResponseDtoList;
    }

    public PostResponseDto update(Long id, PostRequestDto requestDto, HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Post post = postRepository.findById(id).orElseThrow(
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
                post.update(requestDto);
                return new PostResponseDto(post);

            } else {
                throw new IllegalArgumentException("수정 권한이 없습니다");
            }
        }
        throw new IllegalArgumentException("토큰 없음");

    }

    public void delete(Long id,HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        Post post = postRepository.findById(id).orElseThrow(
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
                postRepository.deleteById(id);
                return;
            } else {
                throw new IllegalArgumentException("삭제 권한이 없습니다");
            }
        }
        throw new IllegalArgumentException("토큰 없음");

        }

    private Post checkPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NullPointerException("게시글 없음")
        );
    }


}