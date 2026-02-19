package com.pakids.yagsogseo.business.activity.event;

import java.time.Instant;
import java.util.Map;

public record ActivityEvent(
    Long userId,
    String actionType,
    String resourceType,
    String resourceId,
    String apiPath,
    String httpMethod,
    String ipAddress,
    String userAgent,
    Instant timestamp,
    Map<String, Object> metadata
) {

}

