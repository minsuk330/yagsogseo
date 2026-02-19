package com.pakids.yagsogseo.global.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.pakids.yagsogseo.security.auth.AuthErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String UNHANDLED_ERROR_CODE = "UNHANDLED_ERROR";
  private static final String UNHANDLED_ERROR_MESSAGE = "예상치 못한 오류가 발생했습니다.";

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResp> handleCustomException(CustomException e) {
    ErrorCode errorCode = e.getErrorCode();
    String detail = e.getDetail();

    ErrorResp errorResp = ErrorResp.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .detail(detail)
        .fieldErrors(null)
        .build();
    return new ResponseEntity<>(errorResp, errorCode.getStatus());
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<ErrorResp> handleAuthenticationException(
      HttpServletRequest request, AuthenticationException e
  ) {
    ErrorCode errorCode = AuthErrorCode.UNAUTHORIZED;
    ErrorResp errorResp = ErrorResp.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .detail(e.getMessage())
        .fieldErrors(null)
        .build();

    return new ResponseEntity<>(errorResp, errorCode.getStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResp> handleAccessDeniedException(HttpServletRequest request,
      AccessDeniedException e) {
    ErrorCode errorCode = AuthErrorCode.FORBIDDEN;
    ErrorResp errorResp = ErrorResp.builder()
        .code(errorCode.getCode())
        .message(errorCode.getMessage())
        .detail(e.getMessage())
        .fieldErrors(null)
        .build();

    return new ResponseEntity<>(errorResp, errorCode.getStatus());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResp> handleRuntimeException(RuntimeException e) {
    ErrorResp errorResp = ErrorResp.builder()
        .code(UNHANDLED_ERROR_CODE)
        .message(UNHANDLED_ERROR_MESSAGE)
        .detail(e.getMessage())
        .fieldErrors(null)
        .stackTrace(null)
        .build();

    return new ResponseEntity<>(errorResp, HttpStatus.BAD_REQUEST);
  }
}
