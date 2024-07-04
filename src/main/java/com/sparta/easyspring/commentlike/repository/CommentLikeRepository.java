
package com.sparta.easyspring.commentlike.repository;


import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.comment.entity.Comment;
import com.sparta.easyspring.commentlike.entity.CommentLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    Page<CommentLike> findAllByUserId(Long id, Pageable pageable);

    int countAllByUserId(Long id);
}
