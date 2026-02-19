package com.pakids.yagsogseo.business.activity.service;

import com.pakids.yagsogseo.domain.activity.repository.UserActivityRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityAnalyticsService {

  private final UserActivityRepository userActivityRepository;

  public Long getDAU(LocalDate date) {
    Instant startOfDay = date.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
    Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();

    return userActivityRepository.countDistinctUserIdByTimestampBetween(startOfDay, endOfDay);
  }

  public Long getMAU(LocalDate startDate, LocalDate endDate) {
    Instant start = startDate.atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();
    Instant end = endDate.plusDays(1).atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant();

    return userActivityRepository.countDistinctUserIdByTimestampBetween(start, end);
  }
}