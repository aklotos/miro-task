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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.aklimenko.miro.model.ratelimit.RateLimitRuleResponse;
import com.aklimenko.miro.model.ratelimit.RateLimitRuleUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Rest controller for rate limit rule administration API. <br>
 * Must be available only for system admins. Allows read existing and specify new rate limit rule
 * parameters.
 */
@RequestMapping(path = "/rateLimitRule")
public interface RateLimitRuleApi {

  /**
   * Read existing rate limit rule.
   *
   * @return {@link RateLimitRuleResponse} json representation wrapped into {@link ResponseEntity}.
   */
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  ResponseEntity<RateLimitRuleResponse> readRateLimitRule();

  /**
   * Update existing rate limit rule with provided parameters.
   *
   * @param rateLimitRuleUpdate Rate limit update request payload.
   * @return {@link RateLimitRuleResponse} json representation wrapped into {@link ResponseEntity}.
   */
  @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  ResponseEntity<RateLimitRuleResponse> updateRateLimitRule(
      @RequestBody final RateLimitRuleUpdateRequest rateLimitRuleUpdate);
}
