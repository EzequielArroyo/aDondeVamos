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
    public void addParticipant(User user) {
        this.validatePostIsInteractable();
        this.validateUserCanJoin(user);
        Participant p = Participant.create(this, user);
        this.participants.add(p);
        this.updateStatus();
    }
    public void removeParticipant(User user) {
        this.validatePostIsInteractable();
        boolean removed = this.participants.removeIf(p ->
                p.getUser().getUuid().equals(user.getUuid())
        );
        if (!removed) {
            throw new EntityNotFoundException("User is not a participant of this post");
        }
        this.updateStatus();
    }
    private void validatePostIsInteractable() {
        if (!this.active) {
            throw new IllegalStateException("Post not available");
        }
        if (this.hasFinished() || this.hasClosed()) {
            throw new IllegalStateException("The post cannot be modified, the post is finished or cancelled");
        }
    }
    private void validateUserCanJoin(User user){
        if(userAlreadyInPost(user)){
            throw new IllegalStateException("User already in post");
        }
        if (this.isFull()) {
            throw new IllegalStateException("Post is full");
        }

    }
    private boolean isFull() {
        return this.participants.size() >= this.maxParticipants;
    }
    private boolean hasFinished() {
        return this.status == PostStatus.FINISHED;
    }
    private boolean hasClosed() {
        return this.status == PostStatus.CANCELLED;
    }
    private boolean userAlreadyInPost(User user) {
        return this.participants.stream().anyMatch(p -> p.getUser().getUuid().equals(user.getUuid()));
    }
    private void updateStatus() {
        if (isFull()) {
            this.status = PostStatus.FULL;
        } else {
            this.status = PostStatus.OPEN;
        }
    }

}
