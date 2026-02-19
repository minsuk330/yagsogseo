package com.pakids.yagsogseo.api.auth.facade;

import com.pakids.yagsogseo.api.auth.dto.request.TossLoginReq;
import com.pakids.yagsogseo.api.auth.dto.response.MemberInfoResp;
import com.pakids.yagsogseo.api.auth.dto.response.TossLoginResp;
import com.pakids.yagsogseo.business.mapper.MemberMapper;
import com.pakids.yagsogseo.business.usecase.auth.TossLoginUseCase;
import com.pakids.yagsogseo.business.usecase.auth.TossLoginUseCase.TossLoginResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthFacade {

  private final TossLoginUseCase tossLoginUseCase;
  private final MemberMapper memberMapper;

  @Transactional
  public TossLoginResp login(TossLoginReq req, HttpServletRequest request) {
    TossLoginResult result = tossLoginUseCase.execute(
        req.getAuthorizationCode(),
        req.getReferrer(),
        request
    );

    MemberInfoResp memberInfo = memberMapper.toResp(result.member());

    return new TossLoginResp(
        result.accessToken(),
        result.refreshToken(),
        memberInfo
    );
  }
}
