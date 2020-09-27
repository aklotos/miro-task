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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.aklimenko.miro.MiroConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RateLimitRuleTest {

  private MiroConfig config = new MiroConfig();

  @Test
  @DisplayName("should not throw when creating widget")
  void shouldNotThrowOnRateLimitRuleCreation() {
    new RateLimitRule(config);
  }

  @Test
  @DisplayName("should return limit for specified target")
  void shouldReturnLimitForSpecifiedTarget() {
    var rateLimit = spy(config.getRateLimit());
    config = mock(MiroConfig.class);
    when(config.getRateLimit()).thenReturn(rateLimit);
    doReturn(777).when(rateLimit).getLimitListWidgets();
    doReturn(333).when(rateLimit).getLimitGlobal();
    doReturn(-1).when(rateLimit).getLimitCreateWidget();
    var rule = new RateLimitRule(config);

    assertThat(rule.getLimit(RateLimitTarget.WIDGETS_LIST), equalTo(777));
    assertThat(rule.getLimit(RateLimitTarget.GLOBAL), equalTo(333));
    assertThat(rule.getLimit(RateLimitTarget.WIDGET_CREATE), equalTo(-1));
  }

  @Test
  @DisplayName("should be modified by nonnull params of rate limit rule update request")
  void shouldBeUpdatedByRateLimitRuleUpdateRequest() {
    var update = new RateLimitRuleUpdateRequest(100000, 100, null, null, null, 20, null);
    var rule = new RateLimitRule(config); // rule with default values
    rule.updateBy(update);

    assertThat(rule.getWindowSizeMS(), equalTo(100000));
    assertThat(rule.getLimit(RateLimitTarget.GLOBAL), equalTo(100));
    assertThat(rule.getLimit(RateLimitTarget.WIDGETS_LIST), equalTo(-1));
    assertThat(rule.getLimit(RateLimitTarget.WIDGET_READ), equalTo(-1));
    assertThat(rule.getLimit(RateLimitTarget.WIDGET_CREATE), equalTo(-1));
    assertThat(rule.getLimit(RateLimitTarget.WIDGET_UPDATE), equalTo(20));
    assertThat(rule.getLimit(RateLimitTarget.WIDGET_DELETE), equalTo(-1));
  }

  @Test
  @DisplayName("should return rate limit rule response with valid params")
  void shouldReturnValidRateLimitRuleResponse() {
    var rateLimit = spy(config.getRateLimit());
    config = mock(MiroConfig.class);
    when(config.getRateLimit()).thenReturn(rateLimit);
    doReturn(777).when(rateLimit).getLimitListWidgets();
    doReturn(333).when(rateLimit).getLimitGlobal();
    doReturn(50000).when(rateLimit).getWindowSizeMS();
    var response = new RateLimitRule(config).toResponse();

    assertThat(response.getWindowSizeMS(), equalTo(50000));
    assertThat(response.getLimitCreateWidget(), equalTo(-1));
    assertThat(response.getLimitDeleteWidget(), equalTo(-1));
    assertThat(response.getLimitGlobal(), equalTo(333));
    assertThat(response.getLimitListWidgets(), equalTo(777));
    assertThat(response.getLimitReadWidget(), equalTo(-1));
    assertThat(response.getLimitUpdateWidget(), equalTo(-1));
  }
}
