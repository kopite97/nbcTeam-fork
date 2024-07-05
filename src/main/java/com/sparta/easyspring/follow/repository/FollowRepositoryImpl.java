package com.sparta.easyspring.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.easyspring.follow.entity.Follow;
import com.sparta.easyspring.follow.entity.QFollow;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    private QFollow follow;

    @Override
    public List<Follow> findRank() {

        return jpaQueryFactory.select(follow)
            .from(follow)
            .groupBy(follow.followingId)
            .orderBy(follow.followingId.count().desc())
            .limit(10)
            .fetch();
    }
}
