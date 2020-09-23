package com.aklimenko.miro.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WidgetUpdateRequestTest {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("should deserialize WidgetUpdateRequest")
  void shouldDeserializeWidgetUpdateRequest() throws JsonProcessingException {
    var json = "{\"x\":1,\"width\":4.0}";
    var widgetUpdate = objectMapper.readValue(json, WidgetUpdateRequest.class);
    assertEquals(1, widgetUpdate.getX());
    assertNull(widgetUpdate.getY());
    assertNull(widgetUpdate.getZ());
    assertEquals(4.0, widgetUpdate.getWidth());
    assertNull(widgetUpdate.getHeight());
  }

  @Test
  @DisplayName("should fail to deserialize empty object")
  void shouldThrowWhenDeserializingEmptyObject() {
    var json = "{}";
    var ex =
        assertThrows(
            ValueInstantiationException.class,
            () -> objectMapper.readValue(json, WidgetUpdateRequest.class));
    assertNotNull(ex.getCause());
    var cause = ex.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals(
        "At least one of the fields 'x', 'y', 'z', 'width' or 'height' must be provided.",
        cause.getMessage());
  }
}
