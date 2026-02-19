package com.pakids.yagsogseo.business.activity.interceptor;

import com.pakids.yagsogseo.business.activity.event.ActionType;
import com.pakids.yagsogseo.business.activity.event.ActivityEvent;
import com.pakids.yagsogseo.business.activity.event.ActivityEventPublisher;
import com.pakids.yagsogseo.global.utils.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityTrackingInterceptor implements HandlerInterceptor {

  private final ActivityEventPublisher activityPublisher;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (ex == null && response.getStatus() >= 200 && response.getStatus() < 300) {
      Long userId = AuthUtils.getUserId();

      String path = request.getRequestURI();
      if (shouldTrack(path)) {
        try {
          ActivityEvent event = new ActivityEvent(
              userId,
              ActionType.API_CALL.getValue(),
              "",
              "",
              path,
              request.getMethod(),
              extractIpAddress(request),
              request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "",
              Instant.now(),
              extractMetadata(request)
          );

          activityPublisher.publishAsync(event);
        } catch (Exception e) {
          log.warn("Failed to track API activity: {}", path, e);
        }
      }
    }
  }

  private boolean shouldTrack(String path) {
    return !path.startsWith("/actuator")
        && !path.startsWith("/swagger-ui")
        && !path.startsWith("/v3/api-docs");
  }

  private String extractIpAddress(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(xForwardedFor)) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
  }

  private Map<String, Object> extractMetadata(HttpServletRequest request) {
    return new HashMap<>();
  }
}