package com.pakids.yagsogseo.business.activity.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pakids.yagsogseo.domain.activity.entity.UserActivity;
import com.pakids.yagsogseo.domain.activity.repository.UserActivityRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityEventPublisher {

  private final UserActivityRepository userActivityRepository;
  private final ObjectMapper objectMapper;

  @Async("activityExecutor")
  public void publishAsync(ActivityEvent event) {
    try {
      UserActivity activity = toEntity(event);
      userActivityRepository.save(activity);
    } catch (Exception e) {
      log.warn("Failed to save user activity: {}", event, e);
    }
  }

  private UserActivity toEntity(ActivityEvent event) {
    return UserActivity.builder()
        .userId(event.userId())
        .actionType(event.actionType())
        .resourceType(event.resourceType())
        .resourceId(event.resourceId())
        .apiPath(event.apiPath())
        .httpMethod(event.httpMethod())
        .ipAddress(event.ipAddress())
        .userAgent(event.userAgent())
        .timestamp(event.timestamp())
        .metadata(convertToJsonNode(event.metadata()))
        .createdAt(Instant.now())
        .build();
  }

  private JsonNode convertToJsonNode(java.util.Map<String, Object> metadata) {
    if (metadata == null || metadata.isEmpty()) {
      return null;
    }
    return objectMapper.valueToTree(metadata);
  }
}