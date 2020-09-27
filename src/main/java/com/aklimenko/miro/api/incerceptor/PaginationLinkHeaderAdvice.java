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

import com.aklimenko.miro.model.pagination.Page;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.util.UriComponentsBuilder;

/** Response interceptor to add pagination Link header if required. */
@ControllerAdvice
public class PaginationLinkHeaderAdvice implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(
      MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(
      Object body,
      MethodParameter returnType,
      MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request,
      ServerHttpResponse response) {
    if (body instanceof Page) {
      final Page<?> page = (Page<?>) body;
      page.getAfterId().ifPresent(afterId -> setLinkHeader(request, response, afterId));
      return page;
    }
    return body;
  }

  /**
   * Sets next 'Link' header to return to a user.
   *
   * @param request Initial request.
   * @param response Response to modify.
   * @param afterId Param to include into link ref to perform next page search.
   */
  private void setLinkHeader(
      final ServerHttpRequest request, final ServerHttpResponse response, final String afterId) {
    response.getHeaders().add("Link", buildLinkHeader(request, afterId));
  }

  private String buildLinkHeader(final ServerHttpRequest request, final String afterId) {
    final String uri =
        UriComponentsBuilder.fromUri(request.getURI())
            .replaceQueryParam("afterId", afterId)
            .build()
            .toUriString();
    return "<" + uri + ">; rel=\"next\"";
  }
}
