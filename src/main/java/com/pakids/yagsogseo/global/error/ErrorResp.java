package com.pakids.yagsogseo.global.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResp {

  @Schema(description = "에러 코드", example = "MEMBER_NOT_FOUND")
  private String code;

  @Schema(description = "에러 메시지", example = "회원을 찾을 수 없습니다")
  private String message;

  @Schema(description = "상세 에러 메시지", example = "ID가 1인 회원이 존재하지 않습니다", required = false)
  private String detail;

  @Schema(description = "필드별 에러 메시지", example = "{\"email\": \"이메일 형식이 올바르지 않습니다\"}", required = false)
  private Map<String, String> fieldErrors;

  @Schema(description = "스택 트레이스 (개발 환경에서만 제공)", required = false)
  private List<String> stackTrace;
}
