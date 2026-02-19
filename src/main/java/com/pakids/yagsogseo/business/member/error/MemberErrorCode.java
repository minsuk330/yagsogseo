package com.pakids.yagsogseo.business.member.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.pakids.yagsogseo.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {

  MEMBER_NOT_FOUND("회원을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  DUPLICATE_TOSS_USER_KEY("이미 가입된 토스 사용자입니다", HttpStatus.CONFLICT);

  private final String message;
  private final HttpStatus status;

  @Override
  public String getCode() {
    return name();
  }
}

