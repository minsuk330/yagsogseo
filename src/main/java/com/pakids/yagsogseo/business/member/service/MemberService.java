package com.pakids.yagsogseo.business.member.service;

import com.pakids.yagsogseo.domain.member.entity.Member;
import com.pakids.yagsogseo.domain.member.repository.MemberRepository;
import com.pakids.yagsogseo.external.toss.TossUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public Member findOrCreateByTossUserKey(TossUserInfo tossUserInfo) {
    return memberRepository.findByProviderId(tossUserInfo.getUserKey())
        .orElseGet(() -> createTossMember(tossUserInfo));
  }

  private Member createTossMember(TossUserInfo tossUserInfo) {
    Member member = Member.createToss(
        tossUserInfo.getEmail(),
        tossUserInfo.getName(),
        tossUserInfo.getPhoneNumber(),
        tossUserInfo.getBirth(),
        tossUserInfo.getGender(),
        tossUserInfo.getUserKey()
    );
    return memberRepository.save(member);
  }
}
