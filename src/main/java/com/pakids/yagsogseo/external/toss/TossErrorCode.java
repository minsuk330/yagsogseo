package com.pakids.yagsogseo.external.toss;

import com.pakids.yagsogseo.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TossErrorCode implements ErrorCode {

  TOSS_OAUTH_FAILED("토스 인증에 실패하였습니다", HttpStatus.BAD_REQUEST),
  ;
  private final String message;
  private final HttpStatus status;

  @Override
  public String getCode() {
    return name();
  }
}
