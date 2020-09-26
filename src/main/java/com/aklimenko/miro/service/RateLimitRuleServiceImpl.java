package com.aklimenko.miro.service;

import com.aklimenko.miro.model.ratelimit.RateLimitRule;
import com.aklimenko.miro.model.ratelimit.RateLimitRuleResponse;
import com.aklimenko.miro.model.ratelimit.RateLimitRuleUpdateRequest;
import org.springframework.stereotype.Service;

/**
 * Not synchronized implementation of {@link RateLimitRuleService} contract. <br>
 * While {@link RateLimitRule} is a shared mutable resource here it is believed that related
 * management API should only be available to admins and write operations are considered to be
 * insignificantly rare.
 */
@Service
public class RateLimitRuleServiceImpl implements RateLimitRuleService {

  private final RateLimitRule rule;

  public RateLimitRuleServiceImpl(final RateLimitRule rule) {
    this.rule = rule;
  }

  @Override
  public RateLimitRuleResponse readRateLimitRule() {
    return rule.toResponse();
  }

  @Override
  public RateLimitRuleResponse updateRateLimitRule(
      final RateLimitRuleUpdateRequest rateLimitUpdate) {
    rule.updateBy(rateLimitUpdate);
    return rule.toResponse();
  }
}
