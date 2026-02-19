package com.pakids.yagsogseo.global.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "단일 객체 응답")
@Getter
public class SingleResp<T> {

  @Schema(description = "응답 데이터")
  private T data;

  public SingleResp(T data) {
    this.data = data;
  }
}