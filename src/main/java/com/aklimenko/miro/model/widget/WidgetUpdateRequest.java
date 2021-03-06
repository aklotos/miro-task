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

import com.aklimenko.miro.exception.RequestValidationException;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

/** Immutable model to represent Widget update request. Deserialized from JSON. */
public class WidgetUpdateRequest {

  @Nullable private final Integer x;

  @Nullable private final Integer y;

  @Nullable private final Integer z;

  @Nullable private final Double width;

  @Nullable private final Double height;

  public WidgetUpdateRequest(
      @JsonProperty("x") @Nullable final Integer x,
      @JsonProperty("y") @Nullable final Integer y,
      @JsonProperty("z") @Nullable final Integer z,
      @JsonProperty("width") @Nullable final Double width,
      @JsonProperty("height") @Nullable final Double height) {
    if (x == null && y == null && z == null && width == null && height == null) {
      throw new RequestValidationException(
          "At least one of the fields 'x', 'y', 'z', 'width' or 'height' must be provided.");
    }

    this.x = x;
    this.y = y;
    this.z = z;
    this.width = width;
    this.height = height;
  }

  @Nullable
  public Integer getX() {
    return x;
  }

  @Nullable
  public Integer getY() {
    return y;
  }

  @Nullable
  public Integer getZ() {
    return z;
  }

  @Nullable
  public Double getWidth() {
    return width;
  }

  @Nullable
  public Double getHeight() {
    return height;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    WidgetUpdateRequest that = (WidgetUpdateRequest) o;

    if (x != null ? !x.equals(that.x) : that.x != null) return false;
    if (y != null ? !y.equals(that.y) : that.y != null) return false;
    if (z != null ? !z.equals(that.z) : that.z != null) return false;
    if (width != null ? !width.equals(that.width) : that.width != null) return false;
    return height != null ? height.equals(that.height) : that.height == null;
  }

  @Override
  public int hashCode() {
    int result = x != null ? x.hashCode() : 0;
    result = 31 * result + (y != null ? y.hashCode() : 0);
    result = 31 * result + (z != null ? z.hashCode() : 0);
    result = 31 * result + (width != null ? width.hashCode() : 0);
    result = 31 * result + (height != null ? height.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "WidgetUpdateRequest{"
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
