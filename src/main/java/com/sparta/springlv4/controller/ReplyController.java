package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.ApiResponseDto;
import com.sparta.springlv4.dto.ReplyRequestDto;
import com.sparta.springlv4.dto.ReplyResponseDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.ReplyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/post/reply")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("")   //댓글 작성
    public ReplyResponseDto createReply(@RequestBody ReplyRequestDto replyRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return replyService.createReply(replyRequestDto, userDetails.getUser());
    }
    @PutMapping("/{replyId}")    //댓글 수정
    public ReplyResponseDto updateReply(@PathVariable Long replyId, @RequestBody ReplyRequestDto replyRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return replyService.updateReply(replyId, replyRequestDto, userDetails.getUser());
    }

    @DeleteMapping("/{replyId}") //댓글 삭제
    public ResponseEntity<ApiResponseDto> deleteReply(@PathVariable long replyId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            replyService.deleteReply(replyId, userDetails.getUser());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok().body(new ApiResponseDto("댓글을 찾을 수 없습니다.", HttpStatus.OK.value()));
        } finally {
        }
        return ResponseEntity.ok().body(new ApiResponseDto("댓글 삭제 성공", HttpStatus.OK.value()));
    }
}
