package com.sparta.easyspring.commentlike.controller;

import com.sparta.easyspring.auth.security.UserDetailsImpl;
import com.sparta.easyspring.comment.dto.CommentResponseDto;
import com.sparta.easyspring.commentlike.service.CommentLikeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PostMapping("/like/comment/{userId}/{commentId}")
    public ResponseEntity<String> likeComment(@PathVariable Long userId,
        @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.likeComment(userId, commentId, userDetails.getUser());
    }

    @DeleteMapping("/unlike/comment/{userId}/{commentId}")
    public ResponseEntity<String> unlikeComment(@PathVariable Long userId,
        @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentLikeService.unlikeComment(userId, commentId, userDetails.getUser());
    }

    @GetMapping("/like/comment")
    public ResponseEntity<List<CommentResponseDto>> getLikeComments(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy) {

        return commentLikeService.getLikeComments(userDetails.getUser(), page, sortBy);
    }
}


