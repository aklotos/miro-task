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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertThat(
        json,
        equalTo(
            "{\"id\":\"123-456\",\"x\":1,\"y\":2,\"z\":3,\"width\":4.0,\"height\":5.0,\"lastModifiedAt\":1600811667066}"));
  }

  @Test
  @DisplayName("should not throw when shifting widget with non max z-index upwards")
  void shouldNotThrowWhenShiftingWidgetUpwards() {
    var widget = new Widget("123-456", 1, 2, 10, 4.0, 5.0, Instant.ofEpochMilli(1600811667066L));
    var shifted = widget.shiftUpward();
    assertThat(shifted, not(equalTo(widget)));
    assertThat(shifted.getId(), equalTo("123-456"));
    assertThat(shifted.getX(), equalTo(1));
    assertThat(shifted.getY(), equalTo(2));
    assertThat(shifted.getWidth(), equalTo(4.0));
    assertThat(shifted.getHeight(), equalTo(5.0));
    assertThat(shifted.getLastModifiedAt().toEpochMilli() > 1600811667066L, is(true));
    assertThat(shifted.getZ(), equalTo(11));
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
    assertThat(updated, not(equalTo(widget)));
    assertThat(updated.getX(), equalTo(100));
    assertThat(updated.getY(), equalTo(2));
    assertThat(updated.getZ(), equalTo(0));
    assertThat(updated.getWidth(), equalTo(200.0));
    assertThat(updated.getHeight(), equalTo(5.0));
  }
}
