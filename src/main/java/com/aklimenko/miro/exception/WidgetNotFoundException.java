package com.aklimenko.miro.exception;

/**
 * Exception to indicate that widget is not found by provided ID. Mapped to HTTP 404 Not found
 * response.
 */
public class WidgetNotFoundException extends RuntimeException {

  public WidgetNotFoundException(String id) {
    super("Widget [id=" + id + "] not found.");
  }
}
