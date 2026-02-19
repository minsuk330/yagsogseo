package com.pakids.yagsogseo.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

  String getCode();
  String getMessage();
  HttpStatus getStatus();
}
