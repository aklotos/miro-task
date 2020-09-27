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
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
    assertThat(widgetCreate.getX(), equalTo(1));
    assertThat(widgetCreate.getY(), equalTo(2));
    assertThat(widgetCreate.getZ(), equalTo(3));
    assertThat(widgetCreate.getWidth(), equalTo(4.0));
    assertThat(widgetCreate.getHeight(), equalTo(5.0));
  }

  @Test
  @DisplayName("should deserialize WidgetCreateRequest without z-index")
  void shouldDeserializeWidgetCreateRequestWithoutZ() throws JsonProcessingException {
    var json = "{\"x\":1,\"y\":2,\"width\":4.0,\"height\":5.0}";
    var widgetCreate = objectMapper.readValue(json, WidgetCreateRequest.class);
    assertThat(widgetCreate.getX(), equalTo(1));
    assertThat(widgetCreate.getY(), equalTo(2));
    assertThat(widgetCreate.getZ(), nullValue());
    assertThat(widgetCreate.getWidth(), equalTo(4.0));
    assertThat(widgetCreate.getHeight(), equalTo(5.0));
  }

  @Test
  @DisplayName("should deserialize WidgetCreateRequest without z-index")
  void shouldThrowWhenDeserializingWidgetCreateRequestWithMissingField() {
    var json = "{\"y\":2,\"width\":4.0,\"height\":5.0}";
    var ex =
        assertThrows(
            ValueInstantiationException.class,
            () -> objectMapper.readValue(json, WidgetCreateRequest.class));
    assertThat(ex.getCause(), notNullValue());
    var cause = ex.getCause();
    assertThat(cause.getClass(), equalTo(RequestValidationException.class));
    assertThat(cause.getMessage(), equalTo("Field 'x' must be provided."));
  }

  @Test
  @DisplayName("should create new widget from WidgetCreateRequest")
  void shouldCreateNewWidget() {
    var widgetCreate = new WidgetCreateRequest(1, 2, null, 4.0, 5.0);
    var widget = widgetCreate.toNewWidget(100);
    assertThat(widget.getId(), notNullValue());
    assertThat(widget.getLastModifiedAt(), notNullValue());
    assertThat(widget.getX(), equalTo(1));
    assertThat(widget.getY(), equalTo(2));
    assertThat(widget.getZ(), equalTo(100));
    assertThat(widget.getWidth(), equalTo(4.0));
    assertThat(widget.getHeight(), equalTo(5.0));
  }
}
