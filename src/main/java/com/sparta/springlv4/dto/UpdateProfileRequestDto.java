package com.sparta.springlv4.dto;

import lombok.Getter;

@Getter //접근자 자동 생성
public class UpdateProfileRequestDto {
    private String password;
    private String introduce;

}