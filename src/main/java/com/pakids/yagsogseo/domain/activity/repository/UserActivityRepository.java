package com.pakids.yagsogseo.domain.activity.repository;

import com.pakids.yagsogseo.domain.activity.entity.UserActivity;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserActivityRepository extends JpaRepository<UserActivity, Long> {

  @Query("SELECT COUNT(DISTINCT a.userId) FROM UserActivity a WHERE a.timestamp >= :start AND a.timestamp < :end")
  Long countDistinctUserIdByTimestampBetween(@Param("start") Instant start, @Param("end") Instant end);
}
