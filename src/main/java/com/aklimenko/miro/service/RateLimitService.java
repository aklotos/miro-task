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
import com.aklimenko.miro.model.ratelimit.RateLimitStats;
import com.aklimenko.miro.model.ratelimit.RateLimitTarget;

/** Represents rate limit service contract. <br> */
public interface RateLimitService {

  /**
   * Tries to consume specified rate limited target operation. Ensures corresponding rate limit is
   * not exceeded for provided target operation otherwise throws {@link RateLimitExceededException}.
   * Falls back to use {@link RateLimitTarget#GLOBAL} if specified target operation is not limited.
   * If both provided and {@link RateLimitTarget#GLOBAL} target operations are not limited does
   * nothing.
   *
   * @param target Target operation to rate limit.
   */
  void tryConsume(final RateLimitTarget target);

  /**
   * Builds and returns rate limit stats for specified rate limited target operation.
   *
   * @param target Target operation to collection stats for.
   * @return {@link RateLimitStats} for specified target operation.
   */
  RateLimitStats getStats(final RateLimitTarget target);
}
