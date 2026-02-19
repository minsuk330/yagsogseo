package com.pakids.yagsogseo.external.toss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("toss.oauth")
public class TossOauthProperties {

  private String decryptKey;
  private String ADD;
}
