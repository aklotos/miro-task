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
package com.aklimenko.miro.model.pagination;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.aklimenko.miro.model.widget.Widget;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PageTest {

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
  @DisplayName("should not throw when creating a page")
  void shouldNotThrowOnPageCreation() {
    var widget = new Widget("id", 1, 1, 1, 1.0, 1.0, Instant.now());
    var pagination = Pagination.of(10, null);
    new Page<>(Collections.singletonList(widget), pagination, Widget::getId);
  }

  @Test
  @DisplayName("should return limited amount of response entities")
  void shouldReturnLimitedAmountOfResponseEntities() {
    var widgets =
        IntStream.range(0, 10)
            .mapToObj(i -> new Widget(String.valueOf(i), i, i, i, i, i, Instant.now()))
            .collect(Collectors.toUnmodifiableList());
    var pagination = Pagination.of(3, null);
    var page = new Page<>(widgets, pagination, Widget::getId);

    assertThat(page.getResponseEntities().size(), equalTo(3));
  }

  @Test
  @DisplayName("should return afterId when response.size() >= limit")
  void shouldReturnAfterIdWhenResponseSizeNotLessThanLimit() {
    var widgets =
        IntStream.range(0, 10)
            .mapToObj(i -> new Widget(String.valueOf(i), i, i, i, i, i, Instant.now()))
            .collect(Collectors.toUnmodifiableList());
    var pagination = Pagination.of(3, null);
    var page = new Page<>(widgets, pagination, Widget::getId);

    assertThat(page.getAfterId().get(), equalTo(widgets.get(2).getId()));
  }

  @Test
  @DisplayName("should return empty afterId when response.size() < limit")
  void shouldReturnEmptyAfterIdWhenResponseSizeLessThanLimit() {
    var widgets =
        IntStream.range(0, 3)
            .mapToObj(i -> new Widget(String.valueOf(i), i, i, i, i, i, Instant.now()))
            .collect(Collectors.toUnmodifiableList());
    var pagination = Pagination.of(5, null);
    var page = new Page<>(widgets, pagination, Widget::getId);

    assertThat(page.getAfterId(), equalTo(Optional.empty()));
  }

  @Test
  @DisplayName("should serialize page into entities array json")
  void shouldSerializePageIntoEntitiesArray() throws JsonProcessingException {
    var timestamp = Instant.ofEpochMilli(1601200349538L);
    var widgets =
        IntStream.range(0, 3)
            .mapToObj(i -> new Widget(String.valueOf(i), i, i, i, i, i, timestamp))
            .collect(Collectors.toUnmodifiableList());
    var pagination = Pagination.of(5, null);
    var page = new Page<>(widgets, pagination, Widget::getId);
    var json = objectMapper.writeValueAsString(page);

    assertThat(
        json,
        equalTo(
            "[{\"id\":\"0\",\"x\":0,\"y\":0,\"z\":0,\"width\":0.0,\"height\":0.0,\"lastModifiedAt\":1601200349538},{\"id\":\"1\",\"x\":1,\"y\":1,\"z\":1,\"width\":1.0,\"height\":1.0,\"lastModifiedAt\":1601200349538},{\"id\":\"2\",\"x\":2,\"y\":2,\"z\":2,\"width\":2.0,\"height\":2.0,\"lastModifiedAt\":1601200349538}]"));
  }
}
