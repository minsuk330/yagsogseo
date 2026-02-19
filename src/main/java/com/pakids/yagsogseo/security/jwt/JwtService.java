package com.pakids.yagsogseo.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pakids.yagsogseo.security.auth.AuthUser;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private final JwtProperties jwtProperties;

  public String issueAccessToken(AuthUser authUser) {
    Date now = new Date();
    return JWT.create()
        .withIssuer(jwtProperties.getIssuer())
        .withIssuedAt(now)
        .withExpiresAt(new Date(now.getTime() + jwtProperties.getTokenExpireSecs() * 1000L))
        .withClaim("userId", authUser.getId())
        .withArrayClaim("authorities", authUser.getAuthorities())
        .withClaim("userType", authUser.getAuthUserType().name())
        .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
  }

  public String issueRefreshToken(AuthUser authUser) {
    Date now = new Date();
    return JWT.create()
        .withIssuer(jwtProperties.getIssuer())
        .withIssuedAt(now)
        .withExpiresAt(new Date(now.getTime() + jwtProperties.getRefreshExpireSecs() * 1000L))
        .withClaim("userId", authUser.getId())
        .withArrayClaim("authorities", authUser.getAuthorities())
        .withClaim("userType", authUser.getAuthUserType().name())
        .sign(Algorithm.HMAC512(jwtProperties.getSecret()));
  }

  public Optional<DecodedJWT> verifyToken(String token) {
    try {
      return Optional.of(JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
          .withIssuer(jwtProperties.getIssuer())
          .build()
          .verify(token));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  public Optional<Long> getUserId(String token) {
    return verifyToken(token)
        .map(jwt -> jwt.getClaim("userId").asLong());
  }

  public Optional<String[]> getAuthorities(String token) {
    return verifyToken(token)
        .map(jwt -> jwt.getClaim("authorities").asArray(String.class));
  }
}
