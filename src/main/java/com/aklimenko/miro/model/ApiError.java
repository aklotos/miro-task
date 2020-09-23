package com.aklimenko.miro.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nonnull;
import org.springframework.http.HttpStatus;

/**
 * Model to represent API error returned to a user on request to REST API. Serialized into
 * JSON response.
 */
public class ApiError {
  @JsonProperty private final int status;

  @JsonProperty @Nonnull private final String error;

  ApiError(@Nonnull final HttpStatus status, @Nonnull final String error) {
    this.status = status.value();
    this.error = error;
  }

  public int getStatus() {
    return status;
  }

  @Nonnull
  public String getError() {
    return error;
  }

  public static ApiError serverError() {
    return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.");
  }

  public static ApiError badRequest(final String errorMsg) {
    return new ApiError(HttpStatus.BAD_REQUEST, errorMsg);
  }

  public static ApiError notFound(final String errorMsg) {
    return new ApiError(HttpStatus.NOT_FOUND, errorMsg);
  }

  public static ApiError conflict(final String errorMsg) {
    return new ApiError(HttpStatus.CONFLICT, errorMsg);
  }
}
