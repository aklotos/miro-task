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

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * Represents rate limit target operations. <br>
 * In order for operation to be rate limited it should be presented in {@link RateLimitTarget} enum.
 * Not presented operations are ignored and rate limit rules are not applied to them. <br>
 */
public enum RateLimitTarget {
  /**
   * Synthetic global target operation to use when specific operation is presented but unlimited.
   */
  GLOBAL("", ""),
  WIDGETS_LIST(HttpMethod.GET.name(), "/widgets"),
  WIDGET_READ(HttpMethod.GET.name(), "/widgets/{id}"),
  WIDGET_CREATE(HttpMethod.POST.name(), "/widgets"),
  WIDGET_UPDATE(HttpMethod.PUT.name(), "/widgets/{id}"),
  WIDGET_DELETE(HttpMethod.DELETE.name(), "/widgets/{id}");

  RateLimitTarget(final String method, final String pathPattern) {
    this.method = method;
    this.pathPattern = PathPatternParser.defaultInstance.parse(pathPattern);
  }

  private final String method;
  private final PathPattern pathPattern;

  private boolean matches(final String requestMethod, final String requestUri) {
    final PathContainer uriContainer = PathContainer.parsePath(requestUri);
    return Objects.equals(this.method, requestMethod) && this.pathPattern.matches(uriContainer);
  }

  /**
   * Tries to resolve combination of request method and request URI into rate limit target operation.
   *
   * @param requestMethod Request method to match with rate limit target operation.
   * @param requestUri Request URI to match with rate limit target operation.
   * @return Resolved {@link RateLimitTarget} wrapped into {@link Optional} when found and {@link
   *     Optional#empty()} when no rate limit target matches specific request.
   */
  public static Optional<RateLimitTarget> of(final String requestMethod, final String requestUri) {
    return Arrays.stream(values()).filter(t -> t.matches(requestMethod, requestUri)).findFirst();
  }
}
