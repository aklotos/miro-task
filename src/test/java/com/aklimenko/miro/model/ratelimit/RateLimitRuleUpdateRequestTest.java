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
package com.aklimenko.miro.model.ratelimit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aklimenko.miro.exception.RequestValidationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RateLimitRuleUpdateRequestTest {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("should deserialize RateLimitUpdateRequest")
  void shouldDeserializeRateLimitUpdateRequest() throws JsonProcessingException {
    var json = "{\"windowSizeMS\":80000,\"limitGlobal\":4000}";
    var request = objectMapper.readValue(json, RateLimitRuleUpdateRequest.class);
    assertThat(request.getWindowSizeMS(), equalTo(80000));
    assertThat(request.getLimitGlobal(), equalTo(4000));
    assertThat(request.getLimitCreateWidget(), nullValue());
    assertThat(request.getLimitDeleteWidget(), nullValue());
    assertThat(request.getLimitListWidgets(), nullValue());
    assertThat(request.getLimitReadWidget(), nullValue());
    assertThat(request.getLimitUpdateWidget(), nullValue());
  }

  @Test
  @DisplayName("should fail to deserialize empty object")
  void shouldThrowWhenDeserializingEmptyObject() {
    var json = "{}";
    var ex =
        assertThrows(
            ValueInstantiationException.class,
            () -> objectMapper.readValue(json, RateLimitRuleUpdateRequest.class));
    assertThat(ex.getCause(), notNullValue());
    var cause = ex.getCause();
    assertThat(cause.getClass(), equalTo(RequestValidationException.class));
    assertThat(
        cause.getMessage(),
        equalTo(
            "At least one of the fields 'windowSizeMS', 'limitGlobal', 'limitListWidgets', 'limitReadWidget', 'limitCreateWidget', 'limitUpdateWidget', 'limitDeleteWidget' must be provided."));
  }
}
