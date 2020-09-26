/*
 Copyright 2020 Anton Klimenko

 <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 except in compliance with the License. You may obtain a copy of the License at

 <p>http://www.apache.org/licenses/LICENSE-2.0

 <p>Unless required by applicable law or agreed to in writing, software distributed under the
 License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 express or implied. See the License for the specific language governing permissions and
 limitations under the License.
*/
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

  public static ApiError tooManyRequests() {
    return new ApiError(HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded.");
  }
}
