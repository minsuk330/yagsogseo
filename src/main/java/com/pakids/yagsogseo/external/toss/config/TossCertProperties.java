package com.pakids.yagsogseo.external.toss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("toss.cert")
public class TossCertProperties {

  private String certPath;
  private String keyPath;
}
