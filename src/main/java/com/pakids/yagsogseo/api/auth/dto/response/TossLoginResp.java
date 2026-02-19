package com.pakids.yagsogseo.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토스 로그인 응답")
public record TossLoginResp(
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzUxMiJ9...")
    String accessToken,

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzUxMiJ9...")
    String refreshToken,

    @Schema(description = "회원 정보")
    MemberInfoResp memberInfo
) {

}

