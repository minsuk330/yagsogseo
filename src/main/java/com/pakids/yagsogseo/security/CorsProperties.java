package com.pakids.yagsogseo.security;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Data
@ConfigurationProperties("security.cors")
public class CorsProperties {

  private List<String> allowedOrigins;
  private List<String> allowedMethods;
  private List<String> allowedHeaders;
  private Long maxAge;

  /**
   * 환경 변수에서 로드된 comma-separated string을 List로 변환합니다.
   * Spring Boot의 @ConfigurationProperties는 YAML 리스트 형식을 자동으로 지원하지만,
   * 환경 변수에서 로드된 comma-separated string은 자동 변환이 안 될 수 있으므로
   * @PostConstruct를 사용하여 수동으로 변환합니다.
   */
  @PostConstruct
  public void parseCommaSeparatedStrings() {
    if (allowedOrigins != null && !allowedOrigins.isEmpty() && allowedOrigins.size() == 1) {
      String origins = allowedOrigins.get(0);
      if (StringUtils.hasText(origins) && origins.contains(",")) {
        allowedOrigins = Arrays.stream(origins.split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .collect(Collectors.toList());
      }
    }

    if (allowedMethods != null && !allowedMethods.isEmpty() && allowedMethods.size() == 1) {
      String methods = allowedMethods.get(0);
      if (StringUtils.hasText(methods) && methods.contains(",")) {
        allowedMethods = Arrays.stream(methods.split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .collect(Collectors.toList());
      }
    }

    if (allowedHeaders != null && !allowedHeaders.isEmpty() && allowedHeaders.size() == 1) {
      String headers = allowedHeaders.get(0);
      if (StringUtils.hasText(headers) && headers.contains(",")) {
        allowedHeaders = Arrays.stream(headers.split(","))
            .map(String::trim)
            .filter(StringUtils::hasText)
            .collect(Collectors.toList());
      } else if ("*".equals(headers.trim())) {
        // "*"는 모든 헤더를 허용하는 의미이므로 그대로 유지
        allowedHeaders = List.of("*");
      }
    }
  }
}
