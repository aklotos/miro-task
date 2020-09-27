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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import com.aklimenko.miro.model.ApiError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ResponseHelperTest {

  @Test
  @DisplayName("should return HTTP 200 OK with response for ok(response)")
  void shouldReturn200WithEntityResponse() {
    var response = ResponseHelper.ok("test response");
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    assertThat(response.getBody(), equalTo("test response"));
  }

  @Test
  @DisplayName("should return HTTP 201 Created with response for created(response)")
  void shouldReturn201WithEntityResponse() {
    var response = ResponseHelper.created("test response");
    assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    assertThat(response.getBody(), equalTo("test response"));
  }

  @Test
  @DisplayName("should return HTTP 204 No content with empty response for noContent()")
  void shouldReturn204WithEmptyResponse() {
    var response = ResponseHelper.noContent();
    assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    assertThat(response.getBody(), nullValue());
  }

  @Test
  @DisplayName("should return HTTP 500 Server error with error message for serverError()")
  void shouldReturn500WithErrorMessage() {
    var response = ResponseHelper.serverError();
    assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
    var apiError = response.getBody();
    assertThat(apiError.getClass(), equalTo(ApiError.class));
    assertThat(apiError.getStatus(), equalTo(500));
    assertThat(apiError.getError(), equalTo("Internal server error."));
  }

  @Test
  @DisplayName("should return HTTP 404 Not found with error message for notFound()")
  void shouldReturn404WithErrorMessage() {
    var response = ResponseHelper.notFound("error message");
    assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    var apiError = response.getBody();
    assertThat(apiError.getClass(), equalTo(ApiError.class));
    assertThat(apiError.getStatus(), equalTo(404));
    assertThat(apiError.getError(), equalTo("error message"));
  }

  @Test
  @DisplayName("should return HTTP 409 Conflict with error message for conflict()")
  void shouldReturn409WithErrorMessage() {
    var response = ResponseHelper.conflict("error message");
    assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
    var apiError = response.getBody();
    assertThat(apiError.getClass(), equalTo(ApiError.class));
    assertThat(apiError.getStatus(), equalTo(409));
    assertThat(apiError.getError(), equalTo("error message"));
  }

  @Test
  @DisplayName("should return HTTP 400 Bad Request with error message for badRequest()")
  void shouldReturn400WithErrorMessage() {
    var response = ResponseHelper.badRequest("error message");
    assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    var apiError = response.getBody();
    assertThat(apiError.getClass(), equalTo(ApiError.class));
    assertThat(apiError.getStatus(), equalTo(400));
    assertThat(apiError.getError(), equalTo("error message"));
  }

  @Test
  @DisplayName("should return HTTP 429 Too many requests for tooManyRequests()")
  void shouldReturn429WithErrorMessage() {
    var response = ResponseHelper.tooManyRequests();
    assertThat(response.getStatusCode(), equalTo(HttpStatus.TOO_MANY_REQUESTS));
    var apiError = response.getBody();
    assertThat(apiError.getClass(), equalTo(ApiError.class));
    assertThat(apiError.getStatus(), equalTo(429));
    assertThat(apiError.getError(), equalTo("Rate limit exceeded."));
  }
}
