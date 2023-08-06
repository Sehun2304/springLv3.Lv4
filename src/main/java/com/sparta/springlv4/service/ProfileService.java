package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.UpdateProfileRequestDto;
import com.sparta.springlv4.dto.UpdatePswdRequestDto;
import com.sparta.springlv4.dto.UserResponseDto;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.UserRepository;
import com.sparta.springlv4.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //서비스 클래스로 선언하면 스프링 컨테이너에 빈으로 등록되어서 다른 컴포넌트에서 @Autowired 등의 방법으로 주입받아서 사용 가능
@RequiredArgsConstructor //클래스 필드에 생성자를 자동 생성
@Slf4j
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User findUser(Long id){
        return userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("잘못된 사용자입니다."));
    }

    //유저 프로필 조회
    public UserResponseDto viewProfile(UserDetailsImpl userDetails) {
        User user = findUser(userDetails.getUser().getId());
        return new UserResponseDto(user);
    }


    public UserResponseDto updateProfile(UserDetailsImpl userDetails, UpdateProfileRequestDto updateProfileRequestDto) {

        User user = findUser(userDetails.getUser().getId());

        //로그인이 되지 않은 상태로 잘못된 접근일 경우
        if (user == null) {
            throw new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다.");
        }

        //비밀번호 재확인
        if (!passwordEncoder.matches(updateProfileRequestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("입력한 비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호가 일치하면 프로필 수정
        String newIntroduce = updateProfileRequestDto.getIntroduce();
        user.setIntroduce(newIntroduce);

        userRepository.save(user);

        return new UserResponseDto(user);



    }

    //비밀번호 변경
    public UserResponseDto updatePswd(UserDetailsImpl userDetails, UpdatePswdRequestDto updatePswdRequestDto) {
        User user = findUser(userDetails.getUser().getId());


        // 비밀번호 변경을 위한 기존 비밀번호 재확인
        if (!passwordEncoder.matches(updatePswdRequestDto.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");}
        else {
            //최근 3회 이내 사용한 비밀번호 목록을 가져옴
            List<String> lastThreePasswords = user.getLastThreePasswords();
            // 입력한 비밀번호가 기존 비밀번호와 일치하면 새로운 비밀번호로 변경 가능
            String newPasswordHash = passwordEncoder.encode(updatePswdRequestDto.getNewPassword());
            // 입력한 새 비밀번호가 최근 3번 이내 사용한 비밀번호인지 확인
            for (String hashedPassword : lastThreePasswords) {
                if (passwordEncoder.matches(updatePswdRequestDto.getNewPassword(), hashedPassword)) {
                    throw new IllegalArgumentException("최근 3번 이내 사용한 비밀번호로는 변경할 수 없습니다");
                }
            }

            //새 비밀번호를 해시에 저장하고 비밀번호 변경 기록을 업데이트
            lastThreePasswords.add(newPasswordHash);
            if(lastThreePasswords.size()>3){
                lastThreePasswords.remove(0);
            }
            //저장되어 있는 비밀번호 갯수가 3개가 넘으면 0번째를 삭제

            user.setPassword(newPasswordHash);
            user.setLastThreePasswords(lastThreePasswords);
            userRepository.save(user);
        }

        return new UserResponseDto(user);
    }

}
