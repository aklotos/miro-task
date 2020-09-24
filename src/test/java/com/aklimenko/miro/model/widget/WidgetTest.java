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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WidgetTest {

  private static ObjectMapper objectMapper;

  @BeforeAll
  static void prepareMapper() {
    objectMapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  @ParameterizedTest
  @DisplayName("should not throw when creating widget")
  @ValueSource(ints = {Integer.MIN_VALUE, -1000, 0, 1000000, Integer.MAX_VALUE})
  void shouldNotThrowOnWidgetCreation(int value) {
    new Widget("123", value, value, value, Double.MIN_VALUE, Double.MAX_VALUE, Instant.now());
  }

  @Test
  @DisplayName("should serialize widget into valid JSON")
  void shouldSerializeIntoValidJSON() throws JsonProcessingException {
    var widget = new Widget("123-456", 1, 2, 3, 4.0, 5.0, Instant.ofEpochMilli(1600811667066L));
    var json = objectMapper.writeValueAsString(widget);
    assertEquals(
        "{\"id\":\"123-456\",\"x\":1,\"y\":2,\"z\":3,\"width\":4.0,\"height\":5.0,\"lastModifiedAt\":1600811667066}",
        json);
  }

  @Test
  @DisplayName("should not throw when shifting widget with non max z-index upwards")
  void shouldNotThrowWhenShiftingWidgetUpwards() {
    var widget = new Widget("123-456", 1, 2, 10, 4.0, 5.0, Instant.ofEpochMilli(1600811667066L));
    var shifted = widget.shiftUpward();
    assertNotEquals(widget, shifted);
    assertEquals("123-456", shifted.getId());
    assertEquals(1, shifted.getX());
    assertEquals(2, shifted.getY());
    assertEquals(4.0, shifted.getWidth());
    assertEquals(5.0, shifted.getHeight());
    assertTrue(1600811667066L < shifted.getLastModifiedAt().toEpochMilli());
    assertEquals(11, shifted.getZ());
  }

  @Test
  @DisplayName("should throw when shifting widget with max z-index upwards")
  void shouldThrowWhenShiftingWidgetWithMaxZIndexUpwards() {
    var widget = new Widget("123-456", 1, 2, Integer.MAX_VALUE, 4.0, 5.0, Instant.now());
    assertThrows(ZIndexLimitExceededException.class, widget::shiftUpward);
  }

  @Test
  @DisplayName("should create new widget with updated fields")
  void shouldPartiallyUpdateWidgetByWidgetUpdateRequest() {
    var widget = new Widget("123-456", 1, 2, Integer.MAX_VALUE, 4.0, 5.0, Instant.now());
    var widgetUpdate = new WidgetUpdateRequest(100, null, 0, 200.0, null);
    var updated = widget.updateBy(widgetUpdate);
    assertNotEquals(widget, updated);
    assertEquals(100, updated.getX());
    assertEquals(2, updated.getY());
    assertEquals(0, updated.getZ());
    assertEquals(200.0, updated.getWidth());
    assertEquals(5.0, updated.getHeight());
  }
}
