package com.pakids.yagsogseo.api.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "토스 로그인 요청")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TossLoginReq {

  @Schema(description = "토스 인증 코드", example = "auth_code_123456", required = true)
  private String authorizationCode;

  @Schema(description = "리퍼러 정보", example = "TEST", required = true)
  private String referrer;
}

