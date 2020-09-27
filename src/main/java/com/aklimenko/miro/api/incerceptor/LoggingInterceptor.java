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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/** Simple logging interceptor to trace each incoming request. */
public class LoggingInterceptor extends HandlerInterceptorAdapter {

  private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(
      final HttpServletRequest request, final HttpServletResponse response, final Object handler)
      throws Exception {
    String url = request.getRequestURI();
    if (request.getQueryString() != null) {
      url += "?" + request.getQueryString();
    }
    log.trace("{} {}", request.getMethod(), url);

    return super.preHandle(request, response, handler);
  }
}
