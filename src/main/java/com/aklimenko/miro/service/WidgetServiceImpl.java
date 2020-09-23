package com.aklimenko.miro.service;

import com.aklimenko.miro.model.Widget;
import com.aklimenko.miro.model.WidgetCreateRequest;
import com.aklimenko.miro.model.WidgetUpdateRequest;
import com.aklimenko.miro.persistence.WidgetRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;

/** Implementation of {@link WidgetService} contract. */
@Service
public class WidgetServiceImpl implements WidgetService {

  private final WidgetRepository widgetRepository;

  public WidgetServiceImpl(final WidgetRepository widgetRepository) {
    this.widgetRepository = widgetRepository;
  }

  @Override
  public Widget createWidget(WidgetCreateRequest widgetToCreate) {
    return widgetRepository.createWidget(widgetToCreate);
  }

  @Override
  public Collection<Widget> listWidgets() {
    return widgetRepository.listWidgets();
  }

  @Override
  public Widget readWidget(String id) {
    return widgetRepository.readWidget(id);
  }

  @Override
  public Widget updateWidget(String id, WidgetUpdateRequest widgetToUpdate) {
    return widgetRepository.updateWidget(id, widgetToUpdate);
  }

  @Override
  public void deleteWidget(String id) {
    widgetRepository.deleteWidget(id);
  }
}
