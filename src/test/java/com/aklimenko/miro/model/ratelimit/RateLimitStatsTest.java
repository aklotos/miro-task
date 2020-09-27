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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RateLimitStatsTest {

  @Test
  @DisplayName("should not throw when creating rate limit stats")
  void shouldNotThrowOnRateLimitStatsCreation() {
    var timestamp = Instant.ofEpochMilli(1601200349538L);
    var stats = RateLimitStats.of(100, 90, timestamp);
    assertThat(stats.getRateLimit(), equalTo("100"));
    assertThat(stats.getAvailableRequests(), equalTo("90"));
    assertThat(stats.getNextReset(), equalTo("1601200349538"));
  }

  @Test
  @DisplayName("should throw when creating with null instant provided")
  void shouldThrowWhenCreatedWithNullInstant() {
    assertThrows(NullPointerException.class, () -> RateLimitStats.of(100, 90, null));
  }

  @Test
  @DisplayName("should create stats for unlimited rate limits")
  void shouldCreateStatForUnlimitedRateLimit() {
    var timestamp = Instant.ofEpochMilli(1601200349538L);
    var stats = RateLimitStats.ofUnlimited(timestamp);
    assertThat(stats.getRateLimit(), equalTo("unlimited"));
    assertThat(stats.getAvailableRequests(), equalTo("unlimited"));
    assertThat(stats.getNextReset(), equalTo("1601200349538"));
  }
}
