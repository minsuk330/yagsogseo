package com.pakids.yagsogseo.global.error;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String detail;

  public CustomException(ErrorCode errorCode, String detail) {
    this.errorCode = errorCode;
    this.detail = detail;
  }

  public CustomException(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.detail = null;
  }
}
