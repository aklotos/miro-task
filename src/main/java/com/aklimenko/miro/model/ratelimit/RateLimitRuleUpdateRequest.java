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
package com.aklimenko.miro.model.ratelimit;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

/** Immutable rate limit update request model deserialized from request JSON payload. */
public class RateLimitRuleUpdateRequest {

  @Nullable private final Integer windowSizeMS;
  @Nullable private final Integer limitGlobal;
  @Nullable private final Integer limitListWidgets;
  @Nullable private final Integer limitReadWidget;
  @Nullable private final Integer limitCreateWidget;
  @Nullable private final Integer limitUpdateWidget;
  @Nullable private final Integer limitDeleteWidget;

  public RateLimitRuleUpdateRequest(
      @JsonProperty("windowSizeMS") @Nullable final Integer windowSizeMS,
      @JsonProperty("limitGlobal") @Nullable final Integer limitGlobal,
      @JsonProperty("limitListWidgets") @Nullable final Integer limitListWidgets,
      @JsonProperty("limitReadWidget") @Nullable final Integer limitReadWidget,
      @JsonProperty("limitCreateWidget") @Nullable final Integer limitCreateWidget,
      @JsonProperty("limitUpdateWidget") @Nullable final Integer limitUpdateWidget,
      @JsonProperty("limitDeleteWidget") @Nullable final Integer limitDeleteWidget) {
    if (windowSizeMS == null
        && limitGlobal == null
        && limitListWidgets == null
        && limitReadWidget == null
        && limitCreateWidget == null
        && limitUpdateWidget == null
        && limitDeleteWidget == null) {
      throw new IllegalArgumentException(
          "At least one of the fields 'windowSizeMS', 'limitGlobal', 'limitListWidgets', 'limitReadWidget', 'limitCreateWidget', 'limitUpdateWidget', 'limitDeleteWidget' must be provided.");
    }
    this.windowSizeMS = windowSizeMS;
    this.limitGlobal = limitGlobal;
    this.limitListWidgets = limitListWidgets;
    this.limitReadWidget = limitReadWidget;
    this.limitCreateWidget = limitCreateWidget;
    this.limitUpdateWidget = limitUpdateWidget;
    this.limitDeleteWidget = limitDeleteWidget;
  }

  public Integer getWindowSizeMS() {
    return windowSizeMS;
  }

  public Integer getLimitGlobal() {
    return limitGlobal;
  }

  public Integer getLimitListWidgets() {
    return limitListWidgets;
  }

  public Integer getLimitReadWidget() {
    return limitReadWidget;
  }

  public Integer getLimitCreateWidget() {
    return limitCreateWidget;
  }

  public Integer getLimitUpdateWidget() {
    return limitUpdateWidget;
  }

  public Integer getLimitDeleteWidget() {
    return limitDeleteWidget;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RateLimitRuleUpdateRequest that = (RateLimitRuleUpdateRequest) o;

    if (windowSizeMS != null ? !windowSizeMS.equals(that.windowSizeMS) : that.windowSizeMS != null)
      return false;
    if (limitGlobal != null ? !limitGlobal.equals(that.limitGlobal) : that.limitGlobal != null)
      return false;
    if (limitListWidgets != null
        ? !limitListWidgets.equals(that.limitListWidgets)
        : that.limitListWidgets != null) return false;
    if (limitReadWidget != null
        ? !limitReadWidget.equals(that.limitReadWidget)
        : that.limitReadWidget != null) return false;
    if (limitCreateWidget != null
        ? !limitCreateWidget.equals(that.limitCreateWidget)
        : that.limitCreateWidget != null) return false;
    if (limitUpdateWidget != null
        ? !limitUpdateWidget.equals(that.limitUpdateWidget)
        : that.limitUpdateWidget != null) return false;
    return limitDeleteWidget != null
        ? limitDeleteWidget.equals(that.limitDeleteWidget)
        : that.limitDeleteWidget == null;
  }

  @Override
  public int hashCode() {
    int result = windowSizeMS != null ? windowSizeMS.hashCode() : 0;
    result = 31 * result + (limitGlobal != null ? limitGlobal.hashCode() : 0);
    result = 31 * result + (limitListWidgets != null ? limitListWidgets.hashCode() : 0);
    result = 31 * result + (limitReadWidget != null ? limitReadWidget.hashCode() : 0);
    result = 31 * result + (limitCreateWidget != null ? limitCreateWidget.hashCode() : 0);
    result = 31 * result + (limitUpdateWidget != null ? limitUpdateWidget.hashCode() : 0);
    result = 31 * result + (limitDeleteWidget != null ? limitDeleteWidget.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "RateLimitRuleUpdateRequest{"
        + "windowSizeMS="
        + windowSizeMS
        + ", limitGlobal="
        + limitGlobal
        + ", limitListWidgets="
        + limitListWidgets
        + ", limitReadWidget="
        + limitReadWidget
        + ", limitCreateWidget="
        + limitCreateWidget
        + ", limitUpdateWidget="
        + limitUpdateWidget
        + ", limitDeleteWidget="
        + limitDeleteWidget
        + '}';
  }
}
