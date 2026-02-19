package com.pakids.yagsogseo.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = extractToken(request);

    if (StringUtils.hasText(token)) {
      authenticate(token);
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private void authenticate(String token) {
    Optional<Long> userIdOpt = jwtService.getUserId(token);
    Optional<String[]> authoritiesOpt = jwtService.getAuthorities(token);

    if (userIdOpt.isPresent() && authoritiesOpt.isPresent()) {
      Long userId = userIdOpt.get();
      String[] authorities = authoritiesOpt.get();

      SecurityContextHolder.getContext()
          .setAuthentication(
              new UsernamePasswordAuthenticationToken(
                  userId,
                  null,
                  Arrays.stream(authorities)
                      .filter(StringUtils::hasText)
                      .map(SimpleGrantedAuthority::new)
                      .toList()
              )
          );
    }
  }
}
