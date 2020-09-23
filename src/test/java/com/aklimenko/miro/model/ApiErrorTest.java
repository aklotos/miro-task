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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ApiErrorTest {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("should serialize ApiError into valid JSON")
  void shouldSerializeApiErrorIntoValidJson() throws JsonProcessingException {
    var apiError = new ApiError(HttpStatus.CONFLICT, "PEBCAK");
    var json = objectMapper.writeValueAsString(apiError);
    assertEquals("{\"status\":409,\"error\":\"PEBCAK\"}", json);
  }
}
