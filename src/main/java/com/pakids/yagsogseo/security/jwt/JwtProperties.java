package com.pakids.yagsogseo.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("security.jwt")
public class JwtProperties {

  private String issuer;
  private String secret;
  private Integer tokenExpireSecs;
  private Integer refreshExpireSecs;
}
