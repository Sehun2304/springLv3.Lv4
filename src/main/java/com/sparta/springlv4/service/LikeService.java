package com.sparta.springlv4.service;

import com.sparta.springlv4.entity.*;
import com.sparta.springlv4.repository.PostLikeRepository;
import com.sparta.springlv4.repository.PostRepository;
import com.sparta.springlv4.repository.ReplyLikeRepository;
import com.sparta.springlv4.repository.ReplyRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {


    private final PostRepository postRepository;

    private final ReplyRepository replyRepository;

    private final PostLikeRepository postLikesRepository;

    private final ReplyLikeRepository replyLikesRepository;


    // 게시글 좋아요
    public void likePost(User user, Long postId) {
        Post post = findPost(postId);
        if (postLikesRepository.findByUserAndPost(user, post).isPresent()){
            throw new DuplicateRequestException("이미 좋아요한 게시글입니다.");
        } else {
            PostLike postLike = new PostLike(user, post);
            postLikesRepository.save(postLike);
        }
    }

    // 게시글 좋아요 삭제
    public void deleteLikePost(User user, Long postId) {
        Post post = findPost(postId);
        Optional<PostLike> findLikePost = postLikesRepository.findByUserAndPost(user, post);
        if(findLikePost.isPresent()) {
            postLikesRepository.delete(findLikePost.get());
        } else {
            throw new IllegalArgumentException("게시글에 취소할 좋아요가 없습니다.");
        }
    }

    // 댓글 좋아요
    public void likeReply(User user, Long replyId) {

        Reply reply = findReply(replyId);

        if (replyLikesRepository.findByUserAndReply(user, reply).isPresent()){
            throw new DuplicateRequestException("이미 좋아요한 댓글입니다.");
        } else {
            ReplyLike replyLike = new ReplyLike(user, reply);
            replyLikesRepository.save(replyLike);
        }
    }

    // 댓글 좋아요 삭제
    public void deleteLikeReply(User user, Long replyId) {
        Reply reply = findReply(replyId);
        Optional<ReplyLike> findLikeReply = replyLikesRepository.findByUserAndReply(user, reply);
        if(findLikeReply.isPresent()) {
            replyLikesRepository.delete(findLikeReply.get());
        } else {
            throw new IllegalArgumentException("댓글에 취소할 좋아요가 없습니다.");
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("선택한 게시글이 존재하지 않습니다."));
    }

    private Reply findReply(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(
                () -> new IllegalArgumentException("선택한 댓글이 존재하지 않습니다."));
    }
}