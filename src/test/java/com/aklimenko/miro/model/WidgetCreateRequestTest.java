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

public class WidgetCreateRequestTest {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  @DisplayName("should deserialize WidgetCreateRequest with z-index")
  void shouldDeserializeWidgetCreateRequestWithZ() throws JsonProcessingException {
    var json = "{\"x\":1,\"y\":2,\"z\":3,\"width\":4.0,\"height\":5.0}";
    var widgetCreate = objectMapper.readValue(json, WidgetCreateRequest.class);
    assertEquals(1, widgetCreate.getX());
    assertEquals(2, widgetCreate.getY());
    assertEquals(3, widgetCreate.getZ());
    assertEquals(4.0, widgetCreate.getWidth());
    assertEquals(5.0, widgetCreate.getHeight());
  }

  @Test
  @DisplayName("should deserialize WidgetCreateRequest without z-index")
  void shouldDeserializeWidgetCreateRequestWithoutZ() throws JsonProcessingException {
    var json = "{\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}";
    var widgetCreate = objectMapper.readValue(json, WidgetCreateRequest.class);
    assertEquals(1, widgetCreate.getX());
    assertEquals(2, widgetCreate.getY());
    assertNull(widgetCreate.getZ());
    assertEquals(4.0, widgetCreate.getWidth());
    assertEquals(5.0, widgetCreate.getHeight());
  }

  @Test
  @DisplayName("should deserialize WidgetCreateRequest without z-index")
  void shouldThrowWhenDeserializingWidgetCreateRequestWithMissingField() {
    var json = "{\"y\":2,\"width\":4.0,\"height\":5.0}";
    var ex =
        assertThrows(
            ValueInstantiationException.class,
            () -> objectMapper.readValue(json, WidgetCreateRequest.class));
    assertNotNull(ex.getCause());
    var cause = ex.getCause();
    assertEquals(IllegalArgumentException.class, cause.getClass());
    assertEquals("Field 'x' must be provided.", cause.getMessage());
  }

  @Test
  @DisplayName("should create new widget from WidgetCreateRequest")
  void shouldCreateNewWidget() {
    var widgetCreate = new WidgetCreateRequest(1, 2, null, 4.0, 5.0);
    var widget = widgetCreate.toNewWidget(100);
    assertNotNull(widget.getId());
    assertNotNull(widget.getLastModifiedAt());
    assertEquals(1, widget.getX());
    assertEquals(2, widget.getY());
    assertEquals(100, widget.getZ());
    assertEquals(4.0, widget.getWidth());
    assertEquals(5.0, widget.getHeight());
  }
}
