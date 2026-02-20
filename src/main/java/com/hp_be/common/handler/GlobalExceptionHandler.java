package com.hp_be.common.handler;

import com.hp_be.common.dto.ApiResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 특정 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResDTO<Void>> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResDTO.error(e.getMessage()));
    }

    // 나머지 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResDTO<Void>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error(e.getMessage()));
    }
}
