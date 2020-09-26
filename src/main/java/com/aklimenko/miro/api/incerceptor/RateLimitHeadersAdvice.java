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
package com.aklimenko.miro.api.incerceptor;

import com.aklimenko.miro.model.ratelimit.RateLimitStats;
import com.aklimenko.miro.model.ratelimit.RateLimitTarget;
import com.aklimenko.miro.service.RateLimitService;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/** Response interceptor to add rate limit headers if required. */
@ControllerAdvice
public class RateLimitHeadersAdvice implements ResponseBodyAdvice<Object> {

  private final RateLimitService rateLimitService;

  public RateLimitHeadersAdvice(RateLimitService rateLimitService) {
    this.rateLimitService = rateLimitService;
  }

  @Override
  public boolean supports(
      final MethodParameter returnType,
      final Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  /**
   * Adds 'X-Rate-Limit', 'X-Available-Requests', 'X-Next-Reset' response headers for rate limited
   * requests.
   */
  @Override
  public Object beforeBodyWrite(
      final Object body,
      final MethodParameter returnType,
      final MediaType selectedContentType,
      final Class<? extends HttpMessageConverter<?>> selectedConverterType,
      final ServerHttpRequest request,
      final ServerHttpResponse response) {
    RateLimitTarget.of(request.getMethodValue(), request.getURI().getPath())
        .map(rateLimitService::getStats)
        .ifPresent(stats -> setRateLimitHeaders(response, stats));

    return body;
  }

  private void setRateLimitHeaders(final ServerHttpResponse response, final RateLimitStats stats) {
    response.getHeaders().add("X-Rate-Limit", stats.getRateLimit());
    response.getHeaders().add("X-Available-Requests", stats.getAvailableRequests());
    response.getHeaders().add("X-Next-Reset", stats.getNextReset());
  }
}
