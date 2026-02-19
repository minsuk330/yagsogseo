package com.pakids.yagsogseo.security.auth;

public interface AuthUser {

  Long getId();
  AuthUserType getAuthUserType();
  String[] getAuthorities();
}
