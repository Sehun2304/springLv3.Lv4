package com.sparta.springlv4.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class ReplyRequestDto {
    Long postId;
    String content;
}