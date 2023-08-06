package com.sparta.springlv4.dto;

import com.sparta.springlv4.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReplyResponseDto extends ApiResponseDto {
    private String content;
    private String username;
    private int replyLikes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReplyResponseDto(Reply reply) {
        super();
        this.content = reply.getContent();
        this.username = reply.getUser().getUsername();
        this.createdAt = reply.getCreatedAt();
        this.modifiedAt = reply.getModifiedAt();
        this.replyLikes = reply.getReplyLikes().size();
    }
}