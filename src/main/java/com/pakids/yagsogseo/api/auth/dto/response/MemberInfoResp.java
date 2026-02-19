package com.pakids.yagsogseo.api.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import com.pakids.yagsogseo.domain.member.entity.Gender;

@Schema(description = "회원 정보 응답")
public record MemberInfoResp(
    @Schema(description = "회원 ID", example = "1")
    Long id,

    @Schema(description = "회원 이름", example = "홍길동")
    String name,

    @Schema(description = "이메일", example = "hong@example.com")
    String email,

    @Schema(description = "생년월일", example = "1990-01-01")
    LocalDate birthDate,

    @Schema(description = "성별", example = "MALE")
    Gender gender
) {

}

