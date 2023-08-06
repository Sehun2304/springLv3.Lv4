package com.sparta.springlv4.service;

import com.sparta.springlv4.dto.PostRequestDto;
import com.sparta.springlv4.dto.PostResponseDto;
import com.sparta.springlv4.entity.Post;
import com.sparta.springlv4.entity.User;
import com.sparta.springlv4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

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

    // 새로운 게시물 생성
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        return mapToResponseDto(savedPost);
    }

    // 특정 게시물 ID로 게시물 정보 수정
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, User user) {
        Post existingPost = findPost(postId);
        log.info("user={}",user);
        log.info("existingPost={}", existingPost.getUser());
        if (!existingPost.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("본인이 작성한 글만 수정할 수 있습니다.");
        }

        existingPost.setTitle(postRequestDto.getTitle());
        existingPost.setContent(postRequestDto.getContent());
        Post updatedPost = postRepository.save(existingPost);
        return mapToResponseDto(updatedPost);
    }

    // 특정 게시물 ID로 게시물 삭제
    public void deletePost(Long postId, User user) {
        Post post = findPost(postId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RejectedExecutionException("본인이 작성한 글만 삭제할 수 있습니다.");
        }
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
        postResponseDto.setPostLikes(post.getPostLikes().size());
        postResponseDto.ChangeReplyResponseDtos(post.getReplies());
        postResponseDto.setTitle(post.getTitle());
        postResponseDto.setContent(post.getContent());
        // 나머지 필드 설정
        return postResponseDto;
    }}
