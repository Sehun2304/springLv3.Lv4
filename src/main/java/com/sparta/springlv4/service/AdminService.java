package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.PostRequestDto;
import com.sparta.springlv4.dto.PostResponseDto;
import com.sparta.springlv4.dto.ReplyRequestDto;
import com.sparta.springlv4.dto.ReplyResponseDto;
import com.sparta.springlv4.entity.Post;
import com.sparta.springlv4.entity.Reply;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.PostRepository;
import com.sparta.springlv4.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {

    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;

    // 모든 게시물 정보 조회
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::mapToResponseDto)// map(post -> mapToResponseDto(post))
                .collect(Collectors.toList());
    }

    // 특정 게시물 ID로 게시물 정보 조회
    public PostResponseDto getPostById(Long postId) {
        Post post = findPost(postId);
        return mapToResponseDto(post);
    }

    // 특정 게시물 ID로 게시물 정보 수정
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post existingPost = findPost(postId);

        existingPost.setTitle(postRequestDto.getTitle());
        existingPost.setContent(postRequestDto.getContent());
        Post updatedPost = postRepository.save(existingPost);
        return mapToResponseDto(updatedPost);
    }

    // 특정 게시물 ID로 게시물 삭제
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);
        postRepository.delete(post);
    }

    // 특정 게시물 ID로 게시물 조회
    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));
    }

    // Post 객체를 PostResponseDto 객체로 변환
    private PostResponseDto mapToResponseDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setPostId(post.getId());

        postResponseDto.setUser(post.getUser().getUsername());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setModifiedAt(post.getModifiedAt());

        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        // 나머지 필드 설정
        return postResponseDto;
    }



    public ReplyResponseDto createReply(ReplyRequestDto requestDto, User user) {//댓글 생성
        Post post = postRepository.findById(requestDto.getPostId()).get();   //PostId로 게시글 찾음
        Reply reply = new Reply(requestDto.getContent(), user, post);  //댓글내용,작성자,작성글 담음

        replyRepository.save(reply);
        return new ReplyResponseDto(reply);
    }
    public ReplyResponseDto updateReply(Long replyId, ReplyRequestDto requestDto, User user) {
        Reply reply = replyRepository.findById(replyId).orElseThrow();
        if (!reply.getUser().getId().equals(user.getId())) {  //작성자와 같은지 체크
            throw new RejectedExecutionException("작성자만 수정 가능합니다");
        }
        reply.setContent(requestDto.getContent());//동일하면 수정댓글을 reply에 넣어줌
        replyRepository.save(reply);
        return new ReplyResponseDto(reply);
    }
    public void deleteReply(Long id, User user) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
        if (!reply.getUser().getId().equals(user.getId())) {  //작성자와 같은지 체크
            throw new RejectedExecutionException("작성자만 삭제 가능합니다");
        }
        replyRepository.delete(reply);


        //1 reply.getUser().equals(user) => 그냥 false !를 쓰면 true로 나옴
        //2 우리가 원하는 것은 정상이면 reply.getUser().equals(user) => 그냥 true !를 쓰면 false로 나옴
    }
}