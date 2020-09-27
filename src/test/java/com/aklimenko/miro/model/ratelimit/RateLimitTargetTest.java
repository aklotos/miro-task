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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class RateLimitTargetTest {

  private static class RequestTarget {
    private final String method;
    private final String uri;
    private RateLimitTarget expected;

    RequestTarget(String method, String uri, RateLimitTarget expected) {
      this.method = method;
      this.uri = uri;
      this.expected = expected;
    }

    @Override
    public String toString() {
      return "RequestTarget{"
          + "method='"
          + method
          + '\''
          + ", uri='"
          + uri
          + '\''
          + ", expected="
          + expected
          + '}';
    }
  }

  static Stream<RequestTarget> validRequests() {
    return Stream.of(
        new RequestTarget("GET", "/widgets", RateLimitTarget.WIDGETS_LIST),
        new RequestTarget(
            "GET", "/widgets/db2ab48b-e258-4045-ad84-5b0ad25b29cc", RateLimitTarget.WIDGET_READ),
        new RequestTarget("POST", "/widgets", RateLimitTarget.WIDGET_CREATE),
        new RequestTarget(
            "PUT", "/widgets/db2ab48b-e258-4045-ad84-5b0ad25b29cc", RateLimitTarget.WIDGET_UPDATE),
        new RequestTarget(
            "DELETE",
            "/widgets/db2ab48b-e258-4045-ad84-5b0ad25b29cc",
            RateLimitTarget.WIDGET_DELETE));
  }

  static Stream<RequestTarget> invalidRequests() {
    return Stream.of(
        new RequestTarget("GET", "/widget", null),
        new RequestTarget("GET", "/widgets/db2ab48b-e258-4045-ad84-5b0ad25b29cc/something", null),
        new RequestTarget("POST", "/widget", null),
        new RequestTarget("PUT", "/widgets", null),
        new RequestTarget(
            "DELETE", "/widgets/db2ab48b-e258-4045-ad84-5b0ad25b29cc/something", null));
  }

  @ParameterizedTest
  @MethodSource("validRequests")
  void shouldReturnValidTarget(final RequestTarget target) {
    var resolvedTarget = RateLimitTarget.of(target.method, target.uri);
    assertThat(resolvedTarget.get(), equalTo(target.expected));
  }

  @ParameterizedTest
  @MethodSource("invalidRequests")
  void shouldReturnEmptyTarget(final RequestTarget target) {
    var resolvedTarget = RateLimitTarget.of(target.method, target.uri);
    assertThat(resolvedTarget, equalTo(Optional.empty()));
  }
}
