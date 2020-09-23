package com.aklimenko.miro.api;

import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.model.WidgetCreateRequest;
import com.aklimenko.miro.model.WidgetUpdateRequest;
import com.aklimenko.miro.service.WidgetService;
import com.aklimenko.miro.utils.ResponseHelper;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of {@link WidgetApi}.
 */
@RestController
public class WidgetApiImpl implements WidgetApi {

  private static final Logger log = LoggerFactory.getLogger(WidgetApiImpl.class);

  private final WidgetService widgetService;

  public WidgetApiImpl(final WidgetService widgetService) {
    this.widgetService = widgetService;
  }

  public ResponseEntity<Collection<Widget>> listWidgets() {
    log.debug("Retrieve list of widgets");

    final Collection<Widget> widgets = widgetService.listWidgets();
    return ResponseHelper.ok(widgets);
  }

  public ResponseEntity<Widget> readWidget(final String id) {
    log.debug("Retrieve widget by id: {}", id);

    final Widget widget = widgetService.readWidget(id);
    return ResponseHelper.ok(widget);
  }

  public ResponseEntity<Widget> createWidget(final WidgetCreateRequest widgetToCreate) {
    log.debug("Create widget: {}", widgetToCreate);

    final Widget widget = widgetService.createWidget(widgetToCreate);
    return ResponseHelper.created(widget);
  }

  public ResponseEntity<Widget> updateWidget(
      final String id, final WidgetUpdateRequest widgetUpdate) {
    log.debug("Update widget by id: {}", id);

    final Widget updatedWidget = widgetService.updateWidget(id, widgetUpdate);
    return ResponseHelper.ok(updatedWidget);
  }

  public ResponseEntity<?> deleteWidget(final String id) {
    log.debug("Delete widget by id: {}", id);

    widgetService.deleteWidget(id);
    return ResponseHelper.noContent();
  }
}
