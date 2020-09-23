package com.aklimenko.miro.model;

import com.aklimenko.miro.utils.ValidationHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Optional;
import javax.annotation.Nonnull;

/**
 * Immutable model to represent Widget to store in the repository and return to a user. Serialized
 * into JSON.
 */
public class Widget {

  @JsonProperty("id")
  @Nonnull
  private final String id;

  @JsonProperty("x")
  private final int x;

  @JsonProperty("y")
  private final int y;

  @JsonProperty("z")
  private final int z;

  @JsonProperty("width")
  private final double width;

  @JsonProperty("height")
  private final double height;

  @JsonProperty("lastModifiedAt")
  @Nonnull
  private final Instant lastModifiedAt;

  public Widget(
      @JsonProperty("id") final String id,
      @JsonProperty("x") int x,
      @JsonProperty("y") int y,
      @JsonProperty("z") int z,
      @JsonProperty("width") double width,
      @JsonProperty("height") double height,
      @JsonProperty("lastModifiedAt") final Instant lastModifiedAt) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.z = z;
    this.width = width;
    this.height = height;
    this.lastModifiedAt = lastModifiedAt;
  }

  public String getId() {
    return id;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getZ() {
    return z;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public Instant getLastModifiedAt() {
    return lastModifiedAt;
  }

  /**
   * Ensures it's safe to shift the widget upwards and returns newly created shifted widget with
   * updated shifted z-index and lastModifiedAt.
   *
   * @return {@link Widget} representing new shifted widget.
   */
  public Widget shiftUpward() {
    ValidationHelper.ensureSafeToPlaceOnTopOf(this);
    return new Widget(id, x, y, z + 1, width, height, Instant.now());
  }

  /**
   * Updates the widget by provided {@link WidgetUpdateRequest}. Creates new {@link Widget} with
   * updated fields and updated lastModifiedAt.
   *
   * @param widgetUpdate Widget update payload.
   * @return {@link Widget} representing new updated widget.
   */
  public Widget updateBy(final WidgetUpdateRequest widgetUpdate) {
    int x = Optional.ofNullable(widgetUpdate.getX()).orElse(this.x);
    int y = Optional.ofNullable(widgetUpdate.getY()).orElse(this.y);
    int z = Optional.ofNullable(widgetUpdate.getZ()).orElse(this.z);
    double width = Optional.ofNullable(widgetUpdate.getWidth()).orElse(this.width);
    double height = Optional.ofNullable(widgetUpdate.getHeight()).orElse(this.height);
    return new Widget(this.id, x, y, z, width, height, Instant.now());
  }
}
