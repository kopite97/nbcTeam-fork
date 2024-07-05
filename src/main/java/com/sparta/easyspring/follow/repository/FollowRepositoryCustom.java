package com.sparta.easyspring.follow.repository;

import com.sparta.easyspring.follow.entity.Follow;
import java.util.List;

public interface FollowRepositoryCustom {

    List<Follow> findRank();
}
