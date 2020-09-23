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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.aklimenko.miro.model.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ResponseHelperTest {

  @Test
  @DisplayName("should return HTTP 200 OK with response for ok(response)")
  void shouldReturn200WithEntityResponse() {
    var response = ResponseHelper.ok("test response");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("test response", response.getBody());
  }

  @Test
  @DisplayName("should return HTTP 201 Created with response for created(response)")
  void shouldReturn201WithEntityResponse() {
    var response = ResponseHelper.created("test response");
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals("test response", response.getBody());
  }

  @Test
  @DisplayName("should return HTTP 204 No content with empty response for noContent()")
  void shouldReturn204WithEmptyResponse() {
    var response = ResponseHelper.noContent();
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  @DisplayName("should return HTTP 500 Server error with error message for serverError()")
  void shouldReturn500WithErrorMessage() {
    var response = ResponseHelper.serverError();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    var apiError = response.getBody();
    assertEquals(ApiError.class, apiError.getClass());
    assertEquals(500, apiError.getStatus());
    assertEquals("Internal server error.", apiError.getError());
  }

  @Test
  @DisplayName("should return HTTP 404 Not found with error message for notFound()")
  void shouldReturn404WithErrorMessage() {
    var response = ResponseHelper.notFound("error message");
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    var apiError = response.getBody();
    assertEquals(ApiError.class, apiError.getClass());
    assertEquals(404, apiError.getStatus());
    assertEquals("error message", apiError.getError());
  }

  @Test
  @DisplayName("should return HTTP 409 Conflict with error message for conflict()")
  void shouldReturn409WithErrorMessage() {
    var response = ResponseHelper.conflict("error message");
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    var apiError = response.getBody();
    assertEquals(ApiError.class, apiError.getClass());
    assertEquals(409, apiError.getStatus());
    assertEquals("error message", apiError.getError());
  }
}
