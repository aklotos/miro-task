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

import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.aklimenko.miro.model.Widget;

/** Helper class to simplify validation of request payload. */
public class ValidationHelper {

  /**
   * Ensures non-null argument is provided.
   *
   * @param arg Argument to validate.
   * @param message Error message if validation fails.
   * @return Provided argument.
   * @throws IllegalArgumentException if null arguments is provided
   */
  public static <T> T requireNonNull(final T arg, final String message) {
    if (arg == null) {
      throw new IllegalArgumentException(message);
    }
    return arg;
  }

  /**
   * Ensures there is still room to place another widget on top of provided one.
   *
   * @param widget Widget with z-index to validate.
   * @throws ZIndexLimitExceededException if z-index limit exceeded.
   */
  public static void ensureSafeToPlaceOnTopOf(final Widget widget) {
    if (widget.getZ() == Integer.MAX_VALUE) {
      throw new ZIndexLimitExceededException(widget.getId());
    }
  }
}
