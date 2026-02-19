package com.pakids.yagsogseo.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum EntityErrorCode implements ErrorCode {
  ENTITY_NOT_FOUND("entity not found", HttpStatus.BAD_REQUEST);
  private final String message;
  private final HttpStatus status;

  @Override
  public String getCode() {
    return name();
  }
}
