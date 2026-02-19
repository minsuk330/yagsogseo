package com.pakids.yagsogseo.api.auth.controller;

import com.pakids.yagsogseo.api.auth.dto.request.TossLoginReq;
import com.pakids.yagsogseo.api.auth.dto.response.TossLoginResp;
import com.pakids.yagsogseo.api.auth.facade.AuthFacade;
import com.pakids.yagsogseo.global.entity.dto.SingleResp;
import com.pakids.yagsogseo.global.error.ErrorResp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "[회원] 로그인/인증")
public class AuthController {
  private final AuthFacade authFacade;

  @PostMapping("/login")
  @Operation(
      summary = "토스 로그인",
      description = "토스 인증 코드를 통해 로그인하고 JWT 토큰을 발급받습니다. 최초 로그인 시 자동으로 회원가입이 진행됩니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "로그인 성공",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(
                  example = """
                      {
                        "data": {
                          "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
                          "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
                          "memberInfo": {
                            "id": 1,
                            "name": "홍길동",
                            "birthDate": "1990-01-01",
                            "gender": "MALE"
                          }
                        }
                      }
                      """
              )
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청 데이터 또는 인증 실패",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResp.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "회원을 찾을 수 없음",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResp.class)
          )
      ),
      @ApiResponse(
          responseCode = "409",
          description = "이미 가입된 토스 사용자",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResp.class)
          )
      )
  })
  public ResponseEntity<SingleResp<TossLoginResp>> login(
      @RequestBody TossLoginReq req,
      HttpServletRequest request
  ) {
    TossLoginResp response = authFacade.login(req, request);

    return ResponseEntity.status(HttpStatus.OK)
        .body(new SingleResp<>(response));
  }

//  @PostMapping("/quit-toss")
//  @Operation(
//      summary = "토스 연결 끊기 콜백",
//      description = "토스에서 호출하는 콜백 URL입니다. 사용자가 토스에서 연결을 끊거나 탈퇴할 때 호출됩니다."
//  )
//  @ApiResponses({
//      @ApiResponse(
//          responseCode = "200",
//          description = "연결 끊기 처리 완료"
//      ),
//      @ApiResponse(
//          responseCode = "400",
//          description = "잘못된 요청 데이터",
//          content = @Content(
//              mediaType = "application/json",
//              schema = @Schema(implementation = ErrorResp.class)
//          )
//      )
//  })
//  public ResponseEntity<String> quitToss(
//      @RequestBody TossQuitReq req
//  ) {
//    memberAuthFacade.quitToss(req.getUserKey(), req.getReferrer());
//    return ResponseEntity.ok("SUCCESS");
//  }

}
