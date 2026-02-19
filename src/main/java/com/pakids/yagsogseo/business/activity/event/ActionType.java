package com.pakids.yagsogseo.business.activity.event;

public enum ActionType {
  API_CALL("API_CALL"),
  TOSS_LOGIN("TOSS_LOGIN");

  private final String value;

  ActionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

