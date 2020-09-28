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
package com.aklimenko.miro.service;

import com.aklimenko.miro.exception.RateLimitExceededException;
import com.aklimenko.miro.model.ratelimit.RateLimitRule;
import com.aklimenko.miro.model.ratelimit.RateLimitStats;
import com.aklimenko.miro.model.ratelimit.RateLimitTarget;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Partially synchronized implementation of the {@link RateLimitService} contract implementing fixed
 * window rate limit algorithm. <br>
 * Depending on the requirements to data integrity and overall system performance rate limit
 * operations in the rate limit service can be fully synchronized, partially synchronized or not
 * synchronized at all. The obvious trade-off of fully synchronized version would be degraded
 * performance of the service with guaranteed integrity of the data. On the other hand fully not
 * synchronized version while being blazing fast can't guarantee data integrity and complicates
 * codebase management. <br>
 * Current version leans to be performant while almost not synchronized :). So it's quite important
 * to understand all the possible side-effects. Below I'm listing potential pitfalls and
 * justifications related to concurrent access to 3 shared resources: {@link #rule}, {@link
 * #requests} and {@link #lastResetAt}.
 *
 * <ul>
 *   <li>While modification of mutable {@link #rule} expected to be insignificantly rare it still
 *       can happen during execution of rate limit operations for another request. In this case
 *       there is a risk to read partially updated {@link #rule} and use it during rate limit
 *       calculations. In this case both decision on rate limit and returned rate limit stats might
 *       be wrong.
 *   <li>While modification of {@link #lastResetAt} happens regularly and can happen concurrently by
 *       multiple threads the next {@link #lastResetAt} does not depends on its previous value so
 *       multiple threads can concurrently set {@link #lastResetAt} to the same new value which
 *       shouldn't be a problem. Additional {@code volatile} modifier prevents caching of the value
 *       in the current thread so all threads see actual up to date value.
 *   <li>Modification of {@link #requests} map internals also happens on a regular basis. Since
 *       {@link #requests} map is not synchronized modifications made in one thread might be not
 *       seen in other parallel thread. But {@link #requests} map has its own specific since it's
 *       initialized on start up and neither key or value references are changed since after
 *       initialization. There are only 2 types of modifications on {@link #requests} data:
 *       increment value and reset value to 0 for specific target key. Both modifications should be
 *       concurrently safe since value is represented by {@link AtomicLong} which is using atomic
 *       CAS operations to modify underlying data.
 * </ul>
 */
@Service
public class RateLimitServiceImpl implements RateLimitService {

  private final RateLimitRule rule;
  private final Map<RateLimitTarget, AtomicLong> requests = new HashMap<>();
  private volatile long lastResetAt;

  public RateLimitServiceImpl(final RateLimitRule rule) {
    this.rule = rule;
    this.lastResetAt = lastWindowReset();
    Arrays.stream(RateLimitTarget.values())
        .forEach(target -> requests.put(target, new AtomicLong(0)));
  }

  @Override
  public void tryConsume(final RateLimitTarget target) {
    final long lastReset = lastWindowReset();
    if (lastResetAt != lastReset) {
      // new rate limit window
      lastResetAt = lastReset;
      resetRequests();
    }

    tryConsumeRequest(target);
  }

  @Override
  public RateLimitStats getStats(final RateLimitTarget target) {
    final int limit = rule.getLimit(target);
    if (target == RateLimitTarget.GLOBAL && limit < 0) {
      return RateLimitStats.ofUnlimited(nextWindowReset());
    } else if (limit < 0) {
      // use global rate limit stat if target operation is unlimited
      return getStats(RateLimitTarget.GLOBAL);
    } else {
      // because of concurrent requests increment they can briefly exceed the actual rate limit
      final long available = Math.max(0, limit - requests.get(target).get());
      return RateLimitStats.of(limit, available, nextWindowReset());
    }
  }

  /**
   * Calculates potential last rate limit window reset timestamp based on current time and window
   * size.
   *
   * @return Timestamp when window was potentially reset last time.
   */
  private long lastWindowReset() {
    final long now = System.currentTimeMillis();
    final long sinceLastReset = now % rule.getWindowSizeMS();
    return now - sinceLastReset;
  }

  /**
   * Calculates next rate limit window reset timestamp based on current time and window size.
   *
   * @return Timestamp when window should be reset next time.
   */
  private long nextWindowReset() {
    return lastWindowReset() + rule.getWindowSizeMS();
  }

  /**
   * Resets requests count. In order to stay concurrently safe it's important that reset should
   * happen by setting existing {@link AtomicInteger} value to 0 instead of putting another {@link
   * AtomicInteger} into map.
   */
  private void resetRequests() {
    Arrays.stream(RateLimitTarget.values()).forEach(target -> requests.get(target).set(0));
  }

  /**
   * Tries to increment requests count for specified rate limited target operation. Ensures requests
   * don't exceed rate limit for provided target operation otherwise throws {@link
   * RateLimitExceededException}. Falls back to use {@link RateLimitTarget#GLOBAL} if specified
   * target operation is not limited. If both provided and {@link RateLimitTarget#GLOBAL} target
   * operations are not limited does nothing. <br>
   * For incrementing requests value uses CAS {@link AtomicInteger#incrementAndGet()} operation.
   *
   * @param target Target operation to increment requests for.
   */
  private void tryConsumeRequest(final RateLimitTarget target) {
    final int limit = rule.getLimit(target);
    if (target == RateLimitTarget.GLOBAL && limit < 0) {
      // do nothing if global operations are unlimited
      return;
    } else if (limit < 0) {
      // use global limit if target operation is unlimited
      tryConsumeRequest(RateLimitTarget.GLOBAL);
    } else {
      final AtomicLong targetRequests = requests.get(target);
      if (targetRequests.get() >= limit) {
        throw new RateLimitExceededException();
      }
      // concurrent safe CAS increment in place
      targetRequests.incrementAndGet();
    }
  }
}
