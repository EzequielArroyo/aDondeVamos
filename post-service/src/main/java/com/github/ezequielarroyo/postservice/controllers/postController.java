package com.github.ezequielarroyo.postservice.controllers;

import com.github.ezequielarroyo.postservice.dtos.input.PostCreateRequest;
import com.github.ezequielarroyo.postservice.dtos.input.PostUpdateRequest;
import com.github.ezequielarroyo.postservice.dtos.output.PostResponse;
import com.github.ezequielarroyo.postservice.services.IPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class postController {
    private final IPostService postService;

    public postController(IPostService postService) {
        this.postService = postService;
    }
    @GetMapping(value = "/ping")
    public String helloWorld(){
        return "PostController respond: pong";
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(Pageable pageable) {
        Page<PostResponse> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts.getContent());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable UUID id){
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostCreateRequest postCreateRequest){
        PostResponse createdPost = postService.createPost(postCreateRequest);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdPost.uuid()).toUri();
        return ResponseEntity.created(uri).body(createdPost);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable UUID id, @RequestBody PostUpdateRequest postUpdateRequest){
        PostResponse updatedPost = postService.updatePost(id, postUpdateRequest);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinPost(@PathVariable UUID id) {
        postService.joinPost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<Void> leavePost(@PathVariable UUID id) {
        postService.leavePost(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable UUID id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();

}
