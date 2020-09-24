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

import com.aklimenko.miro.utils.ValidationHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** Immutable model to represent Widget create request. Deserialized from JSON. */
public class WidgetCreateRequest {

  @Nonnull private final Integer x;

  @Nonnull private final Integer y;

  @Nullable private final Integer z;

  @Nonnull private final Double width;

  @Nonnull private final Double height;

  public WidgetCreateRequest(
      @JsonProperty("x") @Nonnull final Integer x,
      @JsonProperty("y") @Nonnull final Integer y,
      @JsonProperty("z") @Nullable final Integer z,
      @JsonProperty("width") @Nonnull final Double width,
      @JsonProperty("height") @Nonnull final Double height) {
    this.x = ValidationHelper.requireNonNull(x, "Field 'x' must be provided.");
    this.y = ValidationHelper.requireNonNull(y, "Field 'y' must be provided.");
    this.width = ValidationHelper.requireNonNull(width, "Field 'width' must be provided.");
    this.height = ValidationHelper.requireNonNull(height, "Field 'height' must be provided.");
    this.z = z;
  }

  public Integer getX() {
    return x;
  }

  public Integer getY() {
    return y;
  }

  @Nullable
  public Integer getZ() {
    return z;
  }

  public Double getWidth() {
    return width;
  }

  public Double getHeight() {
    return height;
  }

  /**
   * Creates new widget based on {@link WidgetCreateRequest} and provided z-index.
   *
   * @param z Z-index of new widget.
   * @return Newly created {@link Widget}.
   */
  public Widget toNewWidget(int z) {
    final String id = UUID.randomUUID().toString();
    return new Widget(id, x, y, z, width, height, Instant.now());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WidgetCreateRequest that = (WidgetCreateRequest) o;

    if (!x.equals(that.x)) return false;
    if (!y.equals(that.y)) return false;
    if (z != null ? !z.equals(that.z) : that.z != null) return false;
    if (!width.equals(that.width)) return false;
    return height.equals(that.height);
  }

  @Override
  public int hashCode() {
    int result = x.hashCode();
    result = 31 * result + y.hashCode();
    result = 31 * result + (z != null ? z.hashCode() : 0);
    result = 31 * result + width.hashCode();
    result = 31 * result + height.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "WidgetCreateRequest{"
        + "x="
        + x
        + ", y="
        + y
        + ", z="
        + z
        + ", width="
        + width
        + ", height="
        + height
        + '}';
  }
}
