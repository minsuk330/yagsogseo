package com.pakids.yagsogseo.business.mapper;

import com.pakids.yagsogseo.api.auth.dto.response.MemberInfoResp;
import com.pakids.yagsogseo.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

  public MemberInfoResp toResp(Member member) {
    return new MemberInfoResp(
        member.getId(),
        member.getName(),
        member.getEmail(),
        member.getBirthDate(),
        member.getGender()
    );
  }
}
