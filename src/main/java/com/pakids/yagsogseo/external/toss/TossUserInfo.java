package com.pakids.yagsogseo.external.toss;

import com.pakids.yagsogseo.domain.member.entity.Gender;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossUserInfo {

  private String userKey;
  private String name;
  private String email;
  private String phoneNumber;
  private LocalDate birth;
  private Gender gender;
}
