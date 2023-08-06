package com.sparta.springlv4.controller;

import com.sparta.springlv4.dto.ApiResponseDto;
import com.sparta.springlv4.dto.UpdateProfileRequestDto;
import com.sparta.springlv4.dto.UpdatePswdRequestDto;
import com.sparta.springlv4.dto.UserResponseDto;
import com.sparta.springlv4.security.UserDetailsImpl;
import com.sparta.springlv4.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService profilerService;

    //프로필 메인
    @GetMapping
    //보안상 entity보다 dto를 사용하는게 바람직하기 때문에 dto를 만들어서 활용
    public UserResponseDto viewProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info(userDetails.getUsername());
        return profilerService.viewProfile(userDetails);
    }


    //프로필 수정
    @PatchMapping("/update")

    public UserResponseDto updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,@RequestBody UpdateProfileRequestDto updateProfileRequestDto){
        return profilerService.updateProfile(userDetails, updateProfileRequestDto);
    }

    //비밀번호 변경
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDto> updatePswd(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody UpdatePswdRequestDto updatePswdRequestDto){
        try {
            profilerService.updatePswd(userDetails, updatePswdRequestDto);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new ApiResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        return ResponseEntity.ok().body(new ApiResponseDto("변경 성공", HttpStatus.CREATED.value()));
    }
}
