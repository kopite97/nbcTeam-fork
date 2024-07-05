package com.sparta.easyspring.follow.service;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.auth.dto.ProfileResponseDto;
import com.sparta.easyspring.auth.entity.User;
import com.sparta.easyspring.auth.service.UserService;
import com.sparta.easyspring.exception.CustomException;
import com.sparta.easyspring.follow.entity.Follow;
import com.sparta.easyspring.follow.repository.FollowRepository;
import com.sparta.easyspring.follow.repository.FollowRepositoryImpl;
import com.sparta.easyspring.post.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.sparta.easyspring.exception.ErrorEnum.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final FollowRepositoryImpl followRepositoryImpl;
    private final UserService userService;


    public void addFollow(Long followingId, User user) {
        User followingUser = userService.findUserById(followingId);
        if(followingId.equals(user.getId())){
            throw new CustomException(INCORRECT_SELF_FOLLOW);
        }
        Follow checkFollow = findFollowById(followingUser.getId(),user);
        if(checkFollow != null){
            throw new CustomException(ALREADY_FOLLOW);
        }
        Follow follow = new Follow(followingUser,user);
        followRepository.save(follow);
    }

    public void deleteFollow(Long followingId, User user) {
        User followingUser = userService.findUserById(followingId);
        Follow checkFollow = findFollowById(followingUser.getId(),user);
        if(checkFollow == null){
            throw new CustomException(NON_EXISTENT_ELEMENT);
        }
        followRepository.delete(checkFollow);
    }

    public Follow findFollowById(Long followingId, User user){
        return followRepository.findByFollowingIdAndUser(followingId, user);
    }

    public ResponseEntity<List<ProfileResponseDto>> getFollowerRank() {

        List<Follow> followingRankList = followRepositoryImpl.findRank();

        List<ProfileResponseDto> response = new ArrayList<>();
        for (Follow follow : followingRankList) {
            User user = userService.findUserById(follow.getFollowingId());

            int postLikeCount = userService.getPostLikeCount(user.getId());
            int commentLikeCount = userService.getCommentLikeCount(user.getId());

            ProfileResponseDto res = new ProfileResponseDto(user.getId(), user.getUsername(),
                user.getIntroduction(), postLikeCount, commentLikeCount);
            response.add(res);
        }

        return ResponseEntity.ok(response);
    }
}
