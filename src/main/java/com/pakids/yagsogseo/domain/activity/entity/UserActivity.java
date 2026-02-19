package com.pakids.yagsogseo.domain.activity.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_activity")
@Getter
@NoArgsConstructor
public class UserActivity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long userId;
  private String actionType;
  private String resourceType;
  private String resourceId;
  private String apiPath;
  private String httpMethod;
  private String ipAddress;
  private String userAgent;
  private Instant timestamp;

  @Column(columnDefinition = "TEXT")
  @Convert(converter = JsonNodeConverter.class)
  private JsonNode metadata;

  private Instant createdAt;

  @Builder
  private UserActivity(Long userId, String actionType, String resourceType, String resourceId,
      String apiPath, String httpMethod, String ipAddress, String userAgent,
      Instant timestamp, JsonNode metadata, Instant createdAt) {
    this.userId = userId;
    this.actionType = actionType;
    this.resourceType = resourceType;
    this.resourceId = resourceId;
    this.apiPath = apiPath;
    this.httpMethod = httpMethod;
    this.ipAddress = ipAddress;
    this.userAgent = userAgent;
    this.timestamp = timestamp;
    this.metadata = metadata;
    this.createdAt = createdAt;
  }
}
