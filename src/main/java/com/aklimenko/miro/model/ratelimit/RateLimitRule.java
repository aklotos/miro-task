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

import com.aklimenko.miro.MiroConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;

/** Represents rate limit rule mutable model for fixed window rate limit algorithm. */
public class RateLimitRule {

  /** Rate limit fixed size window in milliseconds */
  private int windowSizeMS;

  /** Limits specified for each rate limited target operation. */
  private final Map<RateLimitTarget, Integer> limits = new HashMap<>();

  public RateLimitRule(@Nonnull final MiroConfig config) {
    this(
        config.getRateLimit().getWindowSizeMS(),
        config.getRateLimit().getLimitGlobal(),
        config.getRateLimit().getLimitListWidgets(),
        config.getRateLimit().getLimitReadWidget(),
        config.getRateLimit().getLimitCreateWidget(),
        config.getRateLimit().getLimitUpdateWidget(),
        config.getRateLimit().getLimitDeleteWidget());
  }

  private RateLimitRule(
      final int windowSizeMS,
      final int limitGlobal,
      final int limitListWidgets,
      final int limitReadWidget,
      final int limitCreateWidget,
      final int limitUpdateWidget,
      final int limitDeleteWidget) {
    this.windowSizeMS = windowSizeMS;
    limits.put(RateLimitTarget.GLOBAL, limitGlobal);
    limits.put(RateLimitTarget.WIDGETS_LIST, limitListWidgets);
    limits.put(RateLimitTarget.WIDGET_READ, limitReadWidget);
    limits.put(RateLimitTarget.WIDGET_CREATE, limitCreateWidget);
    limits.put(RateLimitTarget.WIDGET_UPDATE, limitUpdateWidget);
    limits.put(RateLimitTarget.WIDGET_DELETE, limitDeleteWidget);
  }

  public int getWindowSizeMS() {
    return windowSizeMS;
  }

  /**
   * Retrieves rate limit for specific target operation.
   *
   * @param target Target operation to look up limit for.
   * @return Limit for specified target.
   */
  public Integer getLimit(final RateLimitTarget target) {
    return limits.get(target);
  }

  private void putIfNonnull(final Integer limit, final RateLimitTarget target) {
    Optional.ofNullable(limit).ifPresent(l -> limits.put(target, l));
  }

  /**
   * Updates rate limit rule parameters according to specified rate limit rule update request. <br>
   * Mutates the existing model bean.
   *
   * @param rateLimitUpdate Rate limit rule update request payload.
   */
  public void updateBy(final RateLimitRuleUpdateRequest rateLimitUpdate) {
    windowSizeMS = Optional.ofNullable(rateLimitUpdate.getWindowSizeMS()).orElse(windowSizeMS);
    putIfNonnull(rateLimitUpdate.getLimitGlobal(), RateLimitTarget.GLOBAL);
    putIfNonnull(rateLimitUpdate.getLimitListWidgets(), RateLimitTarget.WIDGETS_LIST);
    putIfNonnull(rateLimitUpdate.getLimitReadWidget(), RateLimitTarget.WIDGET_READ);
    putIfNonnull(rateLimitUpdate.getLimitCreateWidget(), RateLimitTarget.WIDGET_CREATE);
    putIfNonnull(rateLimitUpdate.getLimitUpdateWidget(), RateLimitTarget.WIDGET_UPDATE);
    putIfNonnull(rateLimitUpdate.getLimitDeleteWidget(), RateLimitTarget.WIDGET_DELETE);
  }

  /**
   * Builds serializable into JSON rate limit rule response from existing rate limit rule
   * parameters.
   *
   * @return {@link RateLimitRuleResponse} built from existing rate limit rule.
   */
  public RateLimitRuleResponse toResponse() {
    return new RateLimitRuleResponse(
        windowSizeMS,
        limits.get(RateLimitTarget.GLOBAL),
        limits.get(RateLimitTarget.WIDGETS_LIST),
        limits.get(RateLimitTarget.WIDGET_READ),
        limits.get(RateLimitTarget.WIDGET_CREATE),
        limits.get(RateLimitTarget.WIDGET_UPDATE),
        limits.get(RateLimitTarget.WIDGET_DELETE));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RateLimitRule that = (RateLimitRule) o;

    if (windowSizeMS != that.windowSizeMS) return false;
    return limits.equals(that.limits);
  }

  @Override
  public int hashCode() {
    int result = windowSizeMS;
    result = 31 * result + limits.hashCode();
    return result;
  }
}
