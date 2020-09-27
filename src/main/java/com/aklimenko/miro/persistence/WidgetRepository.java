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
package com.aklimenko.miro.persistence;

import com.aklimenko.miro.exception.WidgetNotFoundException;
import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.aklimenko.miro.model.widget.Widget;
import com.aklimenko.miro.model.widget.WidgetCreateRequest;
import com.aklimenko.miro.model.widget.WidgetUpdateRequest;
import java.util.List;
import javax.annotation.Nullable;

/** Contract of widget repository with basic CRUD operations on widgets. */
public interface WidgetRepository {

  /**
   * Create (store) widget in the repository.
   *
   * @param widgetToCreate Validated widget create payload.
   * @return Stored {@link Widget}.
   * @throws ZIndexLimitExceededException if z-index limit exceeded during creation.
   */
  Widget createWidget(WidgetCreateRequest widgetToCreate);

  /**
   * Read requested amount of widgets from repository starting after provided ID and ordered by
   * z-index in ascending order.
   *
   * @param limit Amount of widgets to read.
   * @param afterId Optional ID token to look up for widgets after.
   * @return {@link List<Widget>} ordered by z-index in ascending order.
   */
  List<Widget> listWidgets(int limit, @Nullable String afterId);

  /**
   * Read widget from repository by provided ID.
   *
   * @param id ID to look for widget.
   * @return Found {@link Widget}.
   * @throws WidgetNotFoundException if widget not found by ID.
   */
  Widget readWidget(String id);

  /**
   * Update widget in repository with {@link WidgetUpdateRequest} by provided ID.
   *
   * @param id ID to look for widget to update.
   * @param widgetToUpdate Widget update request payload.
   * @return Updated {@link Widget}.
   * @throws WidgetNotFoundException if widget not found by ID.
   * @throws ZIndexLimitExceededException if z-index limit exceeded during widget update.
   */
  Widget updateWidget(String id, WidgetUpdateRequest widgetToUpdate);

  /**
   * Delete widget from repository by provided ID.
   *
   * @param id ID to look for widget to delete.
   * @throws WidgetNotFoundException if widget not found by ID.
   */
  void deleteWidget(String id);
}
