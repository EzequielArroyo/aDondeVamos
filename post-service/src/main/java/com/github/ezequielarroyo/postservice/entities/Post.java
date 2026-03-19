package com.github.ezequielarroyo.postservice.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;
    @Column(nullable = false)
    private Boolean active;
    @Column(nullable = false, length = 100)
    private String title;
    @Embedded
    private Location location;
    @Column(nullable = false)
    private LocalDateTime activityDate;
    @Column(nullable = false)
    private Integer maxParticipants;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PostStatus status;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "owner_id", referencedColumnName = "id", nullable = false)
    private User owner;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;
    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
    private Instant deletedAt;

    private Post(String title, Location location, LocalDateTime activityDate, Integer maxParticipants,User owner) {
        this.active = true;
        this.uuid = UUID.randomUUID();
        this.status = PostStatus.OPEN;
        this.participants = new ArrayList<>();
        this.title = title;
        this.location = location;
        this.activityDate = activityDate;
        this.maxParticipants = maxParticipants;
        this.owner = owner;

    }
    public static Post create(String title, Location location, LocalDateTime activityDate, Integer maxParticipants, User owner) {
        return new Post(title, location, activityDate, maxParticipants, owner);
    }

}
