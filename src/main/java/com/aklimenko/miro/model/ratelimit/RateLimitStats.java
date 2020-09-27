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

import java.time.Instant;
import javax.annotation.Nonnull;

/**
 * Immutable rate limit stats model representing data returning to a user with rate limited
 * requests.
 */
public class RateLimitStats {

  @Nonnull private final String rateLimit;
  @Nonnull private final String availableRequests;
  @Nonnull private final String nextReset;

  private RateLimitStats(
      @Nonnull final String rateLimit,
      @Nonnull final String availableRequests,
      @Nonnull final String nextReset) {
    this.rateLimit = rateLimit;
    this.availableRequests = availableRequests;
    this.nextReset = nextReset;
  }

  /**
   * Creates rate limit stats representing data returning for unlimited requests.
   *
   * @param nextReset Timestamp of next rate limit window reset.
   * @return {@link RateLimitStats} for unlimited requests.
   */
  public static RateLimitStats ofUnlimited(@Nonnull Instant nextReset) {
    return new RateLimitStats("unlimited", "unlimited", String.valueOf(nextReset.toEpochMilli()));
  }

  public static RateLimitStats of(int rateLimit, int availableRequests, Instant nextReset) {
    return new RateLimitStats(
        String.valueOf(rateLimit),
        String.valueOf(availableRequests),
        String.valueOf(nextReset.toEpochMilli()));
  }

  public String getRateLimit() {
    return rateLimit;
  }

  public String getAvailableRequests() {
    return availableRequests;
  }

  public String getNextReset() {
    return nextReset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RateLimitStats that = (RateLimitStats) o;

    if (!rateLimit.equals(that.rateLimit)) return false;
    if (!availableRequests.equals(that.availableRequests)) return false;
    return nextReset.equals(that.nextReset);
  }

  @Override
  public int hashCode() {
    int result = rateLimit.hashCode();
    result = 31 * result + availableRequests.hashCode();
    result = 31 * result + nextReset.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "RateLimitStats{"
        + "rateLimit='"
        + rateLimit
        + '\''
        + ", availableRequests='"
        + availableRequests
        + '\''
        + ", nextReset='"
        + nextReset
        + '\''
        + '}';
  }
}
