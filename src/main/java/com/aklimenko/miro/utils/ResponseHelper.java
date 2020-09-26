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

  public static ResponseEntity<ApiError> tooManyRequests() {
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(ApiError.tooManyRequests());
  }
}
