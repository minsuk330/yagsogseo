package com.pakids.yagsogseo.external.toss;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pakids.yagsogseo.domain.member.entity.Gender;
import com.pakids.yagsogseo.external.toss.config.TossOauthProperties;
import com.pakids.yagsogseo.global.error.CustomException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TossOauthService {

  private final TossWebClient tossWebClient;
  private final TossOauthProperties tossOauthProperties;
  private final ObjectMapper objectMapper;


  public TossUserInfo getUserInfo(String authorizationCode, String referrer) {

    final String STR_DEFAULT = "";

    try {
      final String GENERATE_TOKEN_URI = "/api-partner/v1/apps-in-toss/user/oauth2/generate-token";

      String tokenResponseRaw = tossWebClient.post()
          .uri(GENERATE_TOKEN_URI)
          .contentType(MediaType.APPLICATION_JSON)
          .bodyValue(Map.of(
              "authorizationCode", authorizationCode,
              "referrer", referrer
          ))
          .retrieve()
          .bodyToMono(String.class)
          .block();

      JsonNode tokenResponse = objectMapper.readTree(tokenResponseRaw);

      if (!tokenResponse.get("resultType").asText(STR_DEFAULT).equals("SUCCESS")) {
        String errorReason = tokenResponse.get("error").get("reason").asText("토스 인증에 실패했습니다");
        throw new IllegalStateException(errorReason);
      }

      String accessToken = tokenResponse.get("success").get("accessToken").asText(STR_DEFAULT);

      final String LOGIN_ME_URI = "/api-partner/v1/apps-in-toss/user/oauth2/login-me";

      String userInfoResponseRaw = tossWebClient.get()
          .uri(LOGIN_ME_URI)
          .header("Authorization", "Bearer " + accessToken)
          .retrive()
          .bodyToMono(String.class)
          .block();

      JsonNode userInfoResponse = objectMapper.readTree(userInfoResponseRaw);

      if (!userInfoResponse.get("resultType").asText(STR_DEFAULT).equals("SUCCESS")) {
        String errorReason = userInfoResponse.get("error").get("reason")
            .asText("사용자 정보를 불러오는데 실패했습니다");
        throw new IllegalStateException(errorReason);
      }

      JsonNode infoBody = userInfoResponse.get("success");

      String providerId = infoBody.get("userKey").asText(null);

      String name = this.decrypt(infoBody.get("name").asText(null));
      String phoneNumber = this.decrypt(infoBody.get("phone").asText(null));
      String email = this.decrypt(infoBody.get("email").asText(null));

      String birthStr = this.decrypt(infoBody.get("birthday").asText(null));
      LocalDate birth = birthStr == null ? null
          : LocalDate.parse(birthStr, DateTimeFormatter.ofPattern("yyyyMMdd"));

      String genderStr = this.decrypt(infoBody.get("gender").asText(null));
      Gender gender = parseGender(genderStr);

      return TossUserInfo.builder()
          .userKey(providerId)
          .name(name)
          .email(email)
          .phoneNumber(phoneNumber)
          .birth(birth)
          .gender(gender)
          .build();

    } catch (Exception e) {
      throw new CustomException(TossErrorCode.TOSS_OAUTH_FAILED, e.getMessage());
    }
  }

  public String decrypt(String encrypted) {
    try {

      final int IV_LENGTH = 12;

      if (encrypted == null || encrypted.length() < IV_LENGTH) {
        return null;
      }

      var decoded = Base64.getDecoder().decode(encrypted);
      var cipher = Cipher.getInstance("AES/GCM/NoPadding");
      var keyByteArray = Base64.getDecoder().decode(tossOauthProperties.getDecryptKey());
      var key = new SecretKeySpec(keyByteArray, "AES");

      var iv = new byte[IV_LENGTH];
      System.arraycopy(decoded, 0, iv, 0, IV_LENGTH);
      var nonceSpec = new GCMParameterSpec((16 * Byte.SIZE), iv);

      cipher.init(Cipher.DECRYPT_MODE, key, nonceSpec);
      cipher.updateAAD(tossOauthProperties.getADD().getBytes());

      byte[] decrypted = cipher.doFinal(decoded, IV_LENGTH, decoded.length - IV_LENGTH);
      return new String(decrypted);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  private Gender parseGender(String genderStr) {
    if (genderStr == null) {
      return null;
    }
    return switch (genderStr.toUpperCase()) {
      case "M", "MALE" -> Gender.MALE;
      case "F", "FEMALE" -> Gender.FEMALE;
      default -> Gender.OTHER;
    };
  }
}