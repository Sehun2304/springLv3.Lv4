package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.ApiResponseDto;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<ApiResponseDto> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        User user = userDetails.getUser();
        try {
            likeService.likePost(user, postId);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("게시글 좋아요 성공", HttpStatus.ACCEPTED.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }


    // 게시글 좋아요 삭제

    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<ApiResponseDto> DeleteLikePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long postId) {
        User user = userDetails.getUser();

        try {
            likeService.deleteLikePost(user, postId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("게시글 좋아요 취소 성공", HttpStatus.ACCEPTED.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }


    //댓글 좋아요

    @PostMapping("/reply/{replyId}/like")
    public ResponseEntity<ApiResponseDto> likeReply(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long replyId) {
        User user = userDetails.getUser();

        try {
            likeService.likeReply(user, replyId);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("댓글 좋아요 성공", HttpStatus.ACCEPTED.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }

    // 댓글 좋아요 삭제 요청
    @DeleteMapping("/reply/{replyId}/like")
    public ResponseEntity<ApiResponseDto> DeleteLikeReply(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long replyId) {
        User user = userDetails.getUser();

        try {
            likeService.deleteLikeReply(user, replyId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        ApiResponseDto apiResponseDto = new ApiResponseDto("댓글 좋아요 삭제 요청", HttpStatus.ACCEPTED.value());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.OK);
    }
}