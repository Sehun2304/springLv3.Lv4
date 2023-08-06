package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Reply;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostResponseDto {
    private Long postId;
    private int state;
    private String user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String title;
    private String content;
    private int postLikes;
    private List<ReplyResponseDto> replyResponseDtos= new ArrayList<>();

    public void ChangeReplyResponseDtos(List<Reply> replies) {
        for (Reply reply : replies) {
            ReplyResponseDto replyResponseDto = new ReplyResponseDto(reply);
            this.replyResponseDtos.add(replyResponseDto);
        }
    }}