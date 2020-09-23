package com.aklimenko.miro.persistence;

import com.aklimenko.miro.exception.WidgetNotFoundException;
import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.model.WidgetCreateRequest;
import com.aklimenko.miro.model.WidgetUpdateRequest;
import java.util.Collection;

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
   * Read all widgets from repository.
   *
   * @return {@link Collection<Widget>} ordered by z-index in ascending order.
   */
  Collection<Widget> listWidgets();

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
