package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.LoginRequestDto;
import com.sparta.springlv4.dto.SignupRequestDto;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.jwt.JwtUtil;
import com.sparta.springlv4.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        });

        userRepository.save(new User(requestDto, password));
    }

    public void Login(LoginRequestDto requestDto, HttpServletResponse response) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록되지 않은 사용자입니다.")
        );

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtUtil.createToken(user.getUsername());
        jwtUtil.addJwtToCookie(token, response);
    }
}
