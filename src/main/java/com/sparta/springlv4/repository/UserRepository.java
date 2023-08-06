package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

//    Optional<User> findByNickname(String nickname);
    //Optional은 값이 있을 수 도 없을 수도 있는 상황에 사용
    //findById는 Jpa를 사용하여 데이터베이스에서 엔티티를 조회하는 메서드

}