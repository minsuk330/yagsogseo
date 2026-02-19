package com.pakids.yagsogseo.business.usecase.auth;

import com.pakids.yagsogseo.business.activity.event.ActionType;
import com.pakids.yagsogseo.business.activity.event.ActivityEvent;
import com.pakids.yagsogseo.business.activity.event.ActivityEventPublisher;
import com.pakids.yagsogseo.business.member.service.MemberService;
import com.pakids.yagsogseo.domain.member.entity.Member;
import com.pakids.yagsogseo.external.toss.TossOauthService;
import com.pakids.yagsogseo.external.toss.TossUserInfo;
import com.pakids.yagsogseo.security.auth.MemberAuthUser;
import com.pakids.yagsogseo.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
@Slf4j
public class TossLoginUseCase {

  private final TossOauthService tossOauthService;
  private final MemberService memberService;
  private final JwtService jwtService;
  private final ActivityEventPublisher activityEventPublisher;

  @Transactional
  public TossLoginResult execute(String authorizationCode, String referrer,
      HttpServletRequest request) {
    log.info("토스 로그인 시도 - authorizationCode: {}", authorizationCode);

    // 1. 토스 사용자 정보 조회
    TossUserInfo tossUserInfo = tossOauthService.getUserInfo(authorizationCode, referrer);
    log.info("토스 사용자 정보 조회 성공 - userKey: {}", tossUserInfo.getUserKey());

    // 2. 회원 조회 또는 생성 (Domain Service 사용)
    Member member = memberService.findOrCreateByTossUserKey(tossUserInfo);

    // 3. JWT 토큰 발급
    MemberAuthUser authUser = new MemberAuthUser(member);
    String accessToken = jwtService.issueAccessToken(authUser);
    String refreshToken = jwtService.issueRefreshToken(authUser);

    log.info("토스 로그인 성공 - memberId: {}", member.getId());

    // 4. 활동 기록
    trackLoginActivity(member.getId(), request);

    return new TossLoginResult(accessToken, refreshToken, member);
  }

  private void trackLoginActivity(Long userId, HttpServletRequest request) {
    try {
      ActivityEvent event = new ActivityEvent(
          userId,
          ActionType.TOSS_LOGIN.getValue(),
          "MEMBER",
          userId.toString(),
          "",
          "",
          extractIpAddress(request),
          request.getHeader("User-Agent") != null ? request.getHeader("User-Agent") : "",
          Instant.now(),
          Map.of()
      );
      activityEventPublisher.publishAsync(event);
    } catch (Exception e) {
      log.warn("Failed to track login activity for userId: {}", userId, e);
    }
  }

  private String extractIpAddress(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (StringUtils.hasText(xForwardedFor)) {
      return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr() != null ? request.getRemoteAddr() : "";
  }

  public record TossLoginResult(
      String accessToken,
      String refreshToken,
      Member member
  ) {
  }
}

