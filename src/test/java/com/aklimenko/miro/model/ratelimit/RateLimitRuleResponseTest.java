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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RateLimitRuleResponseTest {
  private static ObjectMapper objectMapper;

  @BeforeAll
  static void prepareMapper() {
    objectMapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @Test
  @DisplayName("should serialize rate limit rule response into valid JSON")
  void shouldSerializeIntoValidJSON() throws JsonProcessingException {
    var response = new RateLimitRuleResponse(50000, 1000, 100, 200, 300, 400, -1);
    var json = objectMapper.writeValueAsString(response);
    System.out.println(json);
    assertEquals(
        "{\"windowSizeMS\":50000,\"limitGlobal\":1000,\"limitListWidgets\":100,\"limitReadWidget\":200,\"limitCreateWidget\":300,\"limitUpdateWidget\":400,\"limitDeleteWidget\":-1}",
        json);
  }
}
