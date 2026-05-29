package com.github.ezequielarroyo.postservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity @Getter @Setter @NoArgsConstructor
public class Participant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserSnapshot user;
    @CreationTimestamp
    private Instant joinedAt;

    private Participant(Post post, UserSnapshot user) {
        this.post = post;
        this.user = user;
    }
    public static Participant create(Post post, UserSnapshot user) {
        return new Participant(post, user);
    }
}
