package com.pakids.yagsogseo.security.auth;

import com.pakids.yagsogseo.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberAuthUser implements AuthUser {

  private final Member member;

  @Override
  public Long getId() {
    return member.getId();
  }

  @Override
  public AuthUserType getAuthUserType() {
    return AuthUserType.MEMBER;
  }

  @Override
  public String[] getAuthorities() {
    return new String[]{member.getRole().name()};
  }
}
