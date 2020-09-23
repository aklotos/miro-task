package com.aklimenko.miro.exception;

/**
 * Exception to indicate that z-index limit is exceeded.
 * Mapped to HTTP 409 Conflict response.
 */
public class ZIndexLimitExceededException extends RuntimeException {

  public ZIndexLimitExceededException(final String widgetId) {
    super("Z-index limit exceeded. Can not place widget on top of widget [id=" + widgetId + "].");
  }
}
