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
