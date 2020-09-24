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

import com.aklimenko.miro.model.widget.Widget;
import com.aklimenko.miro.model.widget.WidgetCreateRequest;
import com.aklimenko.miro.model.widget.WidgetUpdateRequest;
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
