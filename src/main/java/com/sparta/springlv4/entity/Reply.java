package com.sparta.springlv4.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "Reply")
public class Reply extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "reply", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ReplyLike> replyLikes = new ArrayList<>();

    public Reply(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }
}