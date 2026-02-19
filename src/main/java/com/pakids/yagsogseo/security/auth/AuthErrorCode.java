package com.pakids.yagsogseo.security.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.pakids.yagsogseo.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

  UNAUTHORIZED("인증되지 않은 사용자입니다",HttpStatus.BAD_REQUEST),
  FORBIDDEN("접근 제한됨", HttpStatus.FORBIDDEN),
  ;

  private final String message;
  private final HttpStatus status;

  @Override
  public String getCode() {
    return name();
  }
}
