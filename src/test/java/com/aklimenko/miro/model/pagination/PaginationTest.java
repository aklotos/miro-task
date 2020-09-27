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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aklimenko.miro.exception.RequestValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PaginationTest {

  @Test
  @DisplayName("should not throw when creating valid pagination")
  void shouldNotThrowWhenCreatingValidPagination() {
    Pagination.of(100, "123-456");
    Pagination.of(100, null);
    Pagination.of(null, null);
  }

  @Test
  @DisplayName("should set pagination parameters")
  void shouldSetPaginationParameters() {
    var pagination = Pagination.of(100, "123-456");

    assertThat(pagination.getLimit(), equalTo(100));
    assertThat(pagination.getAfterId(), equalTo("123-456"));
  }

  @Test
  @DisplayName("should set default pagination limit")
  void shouldSetDefaultPaginationLimit() {
    var pagination = Pagination.of(null, null);

    assertThat(pagination.getLimit(), equalTo(Pagination.DEFAULT_LIMIT));
  }

  @Test
  @DisplayName("should throw when pagination limit exceeded")
  void shouldThrowWhenPaginationLimitExceeded() {
    assertThrows(
        RequestValidationException.class, () -> Pagination.of(Pagination.MAX_LIMIT + 1, null));
  }
}
