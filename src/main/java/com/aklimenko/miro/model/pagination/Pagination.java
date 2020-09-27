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
package com.aklimenko.miro.model.pagination;

import com.aklimenko.miro.exception.RequestValidationException;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * Immutable model representing pagination parameters. <br>
 * Defines default and max value for pagination limit.
 */
public class Pagination {
  public static final int DEFAULT_LIMIT = 10;
  public static final int MAX_LIMIT = 500;

  private final int limit;
  @Nullable private final String afterId;

  private Pagination(int limit, final @Nullable String afterId) {
    if (limit > MAX_LIMIT) {
      throw new RequestValidationException("Pagination limit must not exceed " + MAX_LIMIT + ".");
    }

    this.limit = limit;
    this.afterId = afterId;
  }

  public static Pagination of(@Nullable final Integer limit, @Nullable final String afterId) {
    return new Pagination(Optional.ofNullable(limit).orElse(DEFAULT_LIMIT), afterId);
  }

  public int getLimit() {
    return limit;
  }

  @Nullable
  public String getAfterId() {
    return afterId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Pagination that = (Pagination) o;

    if (limit != that.limit) return false;
    return afterId != null ? afterId.equals(that.afterId) : that.afterId == null;
  }

  @Override
  public int hashCode() {
    int result = limit;
    result = 31 * result + (afterId != null ? afterId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Pagination{" + "limit=" + limit + ", afterId='" + afterId + '\'' + '}';
  }
}
