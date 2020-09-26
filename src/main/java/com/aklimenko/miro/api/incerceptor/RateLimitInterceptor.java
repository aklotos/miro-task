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

import com.aklimenko.miro.model.ratelimit.RateLimitTarget;
import com.aklimenko.miro.service.RateLimitService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Rate limit handler interceptor. <br>
 * Applies rate limits rules for requests before they're processed by corresponding controller.
 */
public class RateLimitInterceptor extends HandlerInterceptorAdapter {

  private final RateLimitService rateLimitService;

  public RateLimitInterceptor(RateLimitService rateLimitService) {
    this.rateLimitService = rateLimitService;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    RateLimitTarget.of(request.getMethod(), request.getRequestURI())
        .ifPresent(rateLimitService::tryConsume);

    return super.preHandle(request, response, handler);
  }
}
