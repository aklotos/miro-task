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

/** Immutable rate limit rule model serializable into JSON. */
public class RateLimitRuleResponse {

  @JsonProperty("windowSizeMS")
  private final int windowSizeMS;

  @JsonProperty("limitGlobal")
  private final int limitGlobal;

  @JsonProperty("limitListWidgets")
  private final int limitListWidgets;

  @JsonProperty("limitReadWidget")
  private final int limitReadWidget;

  @JsonProperty("limitCreateWidget")
  private final int limitCreateWidget;

  @JsonProperty("limitUpdateWidget")
  private final int limitUpdateWidget;

  @JsonProperty("limitDeleteWidget")
  private final int limitDeleteWidget;

  public RateLimitRuleResponse(
      int windowSizeMS,
      int limitGlobal,
      int limitListWidgets,
      int limitReadWidget,
      int limitCreateWidget,
      int limitUpdateWidget,
      int limitDeleteWidget) {
    this.windowSizeMS = windowSizeMS;
    this.limitGlobal = limitGlobal;
    this.limitListWidgets = limitListWidgets;
    this.limitReadWidget = limitReadWidget;
    this.limitCreateWidget = limitCreateWidget;
    this.limitUpdateWidget = limitUpdateWidget;
    this.limitDeleteWidget = limitDeleteWidget;
  }

  public int getWindowSizeMS() {
    return windowSizeMS;
  }

  public int getLimitGlobal() {
    return limitGlobal;
  }

  public int getLimitListWidgets() {
    return limitListWidgets;
  }

  public int getLimitReadWidget() {
    return limitReadWidget;
  }

  public int getLimitCreateWidget() {
    return limitCreateWidget;
  }

  public int getLimitUpdateWidget() {
    return limitUpdateWidget;
  }

  public int getLimitDeleteWidget() {
    return limitDeleteWidget;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RateLimitRuleResponse that = (RateLimitRuleResponse) o;

    if (windowSizeMS != that.windowSizeMS) return false;
    if (limitGlobal != that.limitGlobal) return false;
    if (limitListWidgets != that.limitListWidgets) return false;
    if (limitReadWidget != that.limitReadWidget) return false;
    if (limitCreateWidget != that.limitCreateWidget) return false;
    if (limitUpdateWidget != that.limitUpdateWidget) return false;
    return limitDeleteWidget == that.limitDeleteWidget;
  }

  @Override
  public int hashCode() {
    int result = windowSizeMS;
    result = 31 * result + limitGlobal;
    result = 31 * result + limitListWidgets;
    result = 31 * result + limitReadWidget;
    result = 31 * result + limitCreateWidget;
    result = 31 * result + limitUpdateWidget;
    result = 31 * result + limitDeleteWidget;
    return result;
  }

  @Override
  public String toString() {
    return "RateLimitRuleResponse{"
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
