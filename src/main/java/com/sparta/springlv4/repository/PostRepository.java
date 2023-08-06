package com.sparta.springlv4.repository;

import com.sparta.springlv4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
