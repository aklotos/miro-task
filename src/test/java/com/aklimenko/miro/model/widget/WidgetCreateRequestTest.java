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
package com.aklimenko.miro.model.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aklimenko.miro.exception.RequestValidationException;
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
    assertEquals(RequestValidationException.class, cause.getClass());
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
