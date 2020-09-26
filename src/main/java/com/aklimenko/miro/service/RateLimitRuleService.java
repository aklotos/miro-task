package com.aklimenko.miro.service;

import com.aklimenko.miro.model.ratelimit.RateLimitRuleResponse;
import com.aklimenko.miro.model.ratelimit.RateLimitRuleUpdateRequest;

/** Represents rate limit rule service contract. */
public interface RateLimitRuleService {

  /**
   * Read existing rate limit rule parameters.
   *
   * @return {@link RateLimitRuleResponse} serializable into JSON.
   */
  RateLimitRuleResponse readRateLimitRule();

  /**
   * Update existing rate limit rule parameters.
   *
   * @param rateLimitUpdate Rate limit rule update request payload.
   * @return Updated rule in {@link RateLimitRuleResponse} serializable into JSON.
   */
  RateLimitRuleResponse updateRateLimitRule(final RateLimitRuleUpdateRequest rateLimitUpdate);
}
