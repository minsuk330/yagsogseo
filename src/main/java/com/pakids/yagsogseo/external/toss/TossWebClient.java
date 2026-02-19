package com.pakids.yagsogseo.external.toss;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodyUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

@RequiredArgsConstructor
public class TossWebClient {

  private final WebClient webClient;

  public RequestHeadersUriSpec<?> get() {
    return webClient.get();
  }

  public RequestBodyUriSpec post() {
    return webClient.post();
  }
}
