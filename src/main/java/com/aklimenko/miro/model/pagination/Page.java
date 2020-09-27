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

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * Represents immutable model of page of entities returned to a user. Serialized into JSON as array
 * of entities.
 *
 * @param <T> Entity type.
 */
public class Page<T> {
  @Nonnull private final List<T> responseEntities;
  @Nonnull private final Pagination pagination;
  @Nonnull private final Function<T, String> getId;

  public Page(
      final @Nonnull List<T> responseEntities,
      final @Nonnull Pagination pagination,
      final @Nonnull Function<T, String> getId) {
    this.responseEntities = Objects.requireNonNull(responseEntities);
    this.pagination = Objects.requireNonNull(pagination);
    this.getId = Objects.requireNonNull(getId);
  }

  /**
   * @return Unmodifiable list of entities to be serialized into JSON. Ensures the list is capped
   *     according to pagination limit parameter.
   */
  @JsonValue
  @Nonnull
  public List<T> getResponseEntities() {
    return responseEntities.stream()
        .limit(pagination.getLimit())
        .collect(Collectors.toUnmodifiableList());
  }

  /** @return Optional {@code afterID} token param to be included into next ref Link header. */
  @Nonnull
  public Optional<String> getAfterId() {
    if (responseEntities.isEmpty()) {
      return Optional.empty();
    }

    final int responseSize = Math.min(pagination.getLimit(), responseEntities.size());
    final T last = responseEntities.get(responseSize - 1);
    return responseSize == pagination.getLimit()
        ? Optional.of(getId.apply(last))
        : Optional.empty();
  }
}
