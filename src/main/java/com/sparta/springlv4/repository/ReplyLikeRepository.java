package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Reply;
import com.sparta.springlv4.entity.ReplyLike;
import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {

    Optional<ReplyLike> findByUserAndReply(User user, Reply reply);
}