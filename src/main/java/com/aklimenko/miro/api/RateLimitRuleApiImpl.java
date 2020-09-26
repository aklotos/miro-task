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
package com.aklimenko.miro.api;

import com.aklimenko.miro.model.ratelimit.RateLimitRuleResponse;
import com.aklimenko.miro.model.ratelimit.RateLimitRuleUpdateRequest;
import com.aklimenko.miro.service.RateLimitRuleService;
import com.aklimenko.miro.utils.ResponseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Implementation of {@link RateLimitRuleApi}. */
@RestController
public class RateLimitRuleApiImpl implements RateLimitRuleApi {

  private static final Logger log = LoggerFactory.getLogger(RateLimitRuleApiImpl.class);

  private final RateLimitRuleService rateLimitRuleService;

  public RateLimitRuleApiImpl(final RateLimitRuleService rateLimitRuleService) {
    this.rateLimitRuleService = rateLimitRuleService;
  }

  @Override
  public ResponseEntity<RateLimitRuleResponse> readRateLimitRule() {
    log.debug("Read rate limit rule");

    return ResponseHelper.ok(rateLimitRuleService.readRateLimitRule());
  }

  @Override
  public ResponseEntity<RateLimitRuleResponse> updateRateLimitRule(
      final RateLimitRuleUpdateRequest rateLimitRuleUpdate) {
    log.debug("Update rate limit rule: {}", rateLimitRuleUpdate);

    final RateLimitRuleResponse updatedRule =
        rateLimitRuleService.updateRateLimitRule(rateLimitRuleUpdate);
    return ResponseHelper.ok(updatedRule);
  }
}
