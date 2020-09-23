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
