package com.aklimenko.miro.utils;

import com.aklimenko.miro.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/** Helper class to wrap responses into {@link ResponseEntity} and return correct HTTP status. */
public class ResponseHelper {

  public static <T> ResponseEntity<T> ok(final T entity) {
    return ResponseEntity.ok(entity);
  }

  public static <T> ResponseEntity<T> created(final T entity) {
    return ResponseEntity.status(HttpStatus.CREATED).body(entity);
  }

  public static ResponseEntity<?> noContent() {
    return ResponseEntity.noContent().build();
  }

  public static ResponseEntity<ApiError> serverError() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiError.serverError());
  }

  public static ResponseEntity<ApiError> notFound(final String errorMsg) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.notFound(errorMsg));
  }

  public static ResponseEntity<ApiError> conflict(final String errorMsg) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiError.conflict(errorMsg));
  }
}
