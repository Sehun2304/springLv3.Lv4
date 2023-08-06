package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Post;
import com.sparta.springlv4.entity.PostLike;
import com.sparta.springlv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
