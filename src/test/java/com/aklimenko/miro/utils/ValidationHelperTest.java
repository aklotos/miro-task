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
package com.aklimenko.miro.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.aklimenko.miro.model.widget.Widget;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ValidationHelperTest {

  @Test
  @DisplayName("should not throw if non null argument passed into requireNonNull()")
  void nonnullArgumentShouldNotThrow() {
    ValidationHelper.requireNonNull(new Object(), "nullpointer");
    ValidationHelper.requireNonNull("non null string", "nullpointer");
    ValidationHelper.requireNonNull(7, "nullpointer");
  }

  @Test
  @DisplayName("should throw IAE if null argument passed into requireNonNull()")
  void nullArgumentShouldThrow() {
    var exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> ValidationHelper.requireNonNull(null, "error message"));
    assertEquals("error message", exception.getMessage());
  }

  @ParameterizedTest
  @DisplayName(
      "should not throw if widget with non max z-index passed into ensureSafeToPlaceOnTopOf()")
  @ValueSource(ints = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE - 1})
  void nonMaxZIndexShouldNotThrow(int z) {
    var widget = new Widget("id", 1, 1, z, 1, 1, Instant.now());
    ValidationHelper.ensureSafeToPlaceOnTopOf(widget);
  }

  @Test
  @DisplayName("should throw if widget with max z-index passed into ensureSafeToPlaceOnTopOf()")
  void maxZIndexShouldThrow() {
    var widget = new Widget("123", 1, 1, Integer.MAX_VALUE, 1, 1, Instant.now());
    var ex =
        assertThrows(
            ZIndexLimitExceededException.class, () -> ValidationHelper.ensureSafeToPlaceOnTopOf(widget));
    assertEquals(
        "Z-index limit exceeded. Can not place widget on top of widget [id=123].", ex.getMessage());
  }
}
