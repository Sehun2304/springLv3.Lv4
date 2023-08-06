package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.ApiResponseDto;
import com.sparta.springlv4.dto.PostRequestDto;
import com.sparta.springlv4.dto.PostResponseDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 모든 게시글 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 게시글 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long postId) {
        log.info("postId={}", postId);
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto createdPost = postService.createPost(postRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto updatedPost = postService.updatePost(postId, postRequestDto, userDetails.getUser());
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            postService.deletePost(postId, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (RejectedExecutionException e) {
            return ResponseEntity.ok(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok(new ApiResponseDto("게시글 삭제 성공", HttpStatus.OK.value()));
    }
}