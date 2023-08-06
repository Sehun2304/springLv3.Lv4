package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.ReplyRequestDto;
import com.sparta.springlv4.dto.ReplyResponseDto;
import com.sparta.springlv4.entity.Post;
import com.sparta.springlv4.entity.Reply;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.PostRepository;
import com.sparta.springlv4.repository.ReplyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.RejectedExecutionException;

@Service
@AllArgsConstructor

public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;

    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user) {
        Post post = postRepository.findById(requestDto.getPostId()).get();
        Reply reply = new Reply(requestDto.getContent(), user, post);

        replyRepository.save(reply);
        return new ReplyResponseDto(reply);
    }
    public ReplyResponseDto updateReply(Long replyId, ReplyRequestDto requestDto, User user) {
        Reply reply = replyRepository.findById(replyId).orElseThrow();
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("작성자만 수정 가능합니다");
        }
        reply.setContent(requestDto.getContent());
        replyRepository.save(reply);
        return new ReplyResponseDto(reply);
    }
    public void deleteReply(Long id, User user) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
        if (!reply.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("작성자만 삭제 가능합니다");
        }
        replyRepository.delete(reply);

    }
}
