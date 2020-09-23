package com.aklimenko.miro.service;

import com.aklimenko.miro.exception.WidgetNotFoundException;
import com.aklimenko.miro.exception.ZIndexLimitExceededException;
import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.model.WidgetCreateRequest;
import com.aklimenko.miro.model.WidgetUpdateRequest;
import java.util.Collection;

/** Contract of widget service with basic CRUD operations on widgets. */
public interface WidgetService {

  /**
   * Create widget.
   *
   * @param widgetToCreate Widget create payload.
   * @return Created {@link Widget}.
   * @throws ZIndexLimitExceededException if z-index limit exceeded during creation.
   */
  Widget createWidget(WidgetCreateRequest widgetToCreate);

  /**
   * Read all widgets.
   *
   * @return {@link Collection<Widget>} ordered by z-index in ascending order.
   */
  Collection<Widget> listWidgets();

  /**
   * Read widget by provided ID.
   *
   * @param id ID to look for widget.
   * @return Found {@link Widget}.
   * @throws WidgetNotFoundException if widget not found by ID.
   */
  Widget readWidget(String id);

  /**
   * Update widget with {@link WidgetUpdateRequest} by provided ID.
   *
   * @param id ID to look for widget to update.
   * @param widgetToUpdate Widget update request payload.
   * @return Updated {@link Widget}.
   * @throws WidgetNotFoundException if widget not found by ID.
   * @throws ZIndexLimitExceededException if z-index limit exceeded during widget update.
   */
  Widget updateWidget(String id, WidgetUpdateRequest widgetToUpdate);

  /**
   * Delete widget by provided ID.
   *
   * @param id ID to look for widget to delete.
   * @throws WidgetNotFoundException if widget not found by ID.
   */
  void deleteWidget(String id);
}
