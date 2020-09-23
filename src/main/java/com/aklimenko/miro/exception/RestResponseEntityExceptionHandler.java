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
package com.aklimenko.miro.exception;

import com.aklimenko.miro.model.ApiError;
import com.aklimenko.miro.utils.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Root handler for application exceptions. Maps java core exceptions as well as custom exceptions
 * into {@link ApiError}.
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  private static final Logger log =
      LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      final HttpMessageNotReadableException ex,
      final HttpHeaders headers,
      final HttpStatus status,
      final WebRequest request) {

    String errorMsg = "Invalid JSON input.";
    final Throwable cause = ex.getRootCause();
    if (cause instanceof IllegalArgumentException || cause instanceof IllegalStateException) {
      errorMsg = cause.getMessage();
    }

    return handleExceptionInternal(
        ex, ApiError.badRequest(errorMsg), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler({WidgetNotFoundException.class})
  public ResponseEntity<ApiError> handleWidgetNotFoundException(
      final Exception ex, final WebRequest request) {
    log.debug(ex.getMessage());

    return ResponseHelper.notFound(ex.getMessage());
  }

  @ExceptionHandler({ZIndexLimitExceededException.class})
  public ResponseEntity<ApiError> handleZIndexLimitExceededException(
      final Exception ex, final WebRequest request) {
    log.debug(ex.getMessage());

    return ResponseHelper.conflict(ex.getMessage());
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ApiError> handleAllExceptions(
      final Exception ex, final WebRequest request) {
    log.error("Uncaught exception", ex);

    return ResponseHelper.serverError();
  }
}
