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
package com.aklimenko.miro.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.aklimenko.miro.model.widget.Widget;
import com.aklimenko.miro.model.widget.WidgetCreateRequest;
import com.aklimenko.miro.model.widget.WidgetUpdateRequest;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/** Rest API controller for /widgets. */
@RequestMapping(path = "/widgets")
public interface WidgetApi {

  /**
   * {@code GET /widgets}. <br>
   * Lists all available widgets ordered by z-index in ascending order.
   *
   * @return {@link Collection<Widget>} wrapped into {@link ResponseEntity}.
   */
  @GetMapping(produces = APPLICATION_JSON_VALUE)
  ResponseEntity<Collection<Widget>> listWidgets();

  /**
   * {@code GET /widgets/:id}. <br>
   * Reads widget by provided ID.
   *
   * @param id ID to look for widget.
   * @return {@link Widget} wrapped into {@link ResponseEntity}.
   */
  @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  ResponseEntity<Widget> readWidget(@PathVariable("id") final String id);

  /**
   * {@code POST /widgets}. <br>
   * Creates new widget. If z-index is not provided places the widget in the foreground of existing
   * widgets. If z-index is provided and there is existing widget at that z-index than starting from
   * the provided z-index all the widgets are shifted upwards.
   *
   * @param widgetToCreate Widget creation payload.
   * @return {@link Widget} wrapped into {@link ResponseEntity}.
   */
  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  ResponseEntity<Widget> createWidget(@RequestBody final WidgetCreateRequest widgetToCreate);

  /**
   * {@code PUT /widgets/:id}. <br>
   * Updates existing widgets by provided ID. If z-index is provided and there is already existing
   * widget and new z-index acts similar to {@link WidgetApi#createWidget} and shifts existing
   * widgets upwards.
   *
   * @param id ID to look for widget to update.
   * @param widgetUpdate Widget update payload.
   * @return {@link Widget} wrapped into {@link ResponseEntity}.
   */
  @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  ResponseEntity<Widget> updateWidget(
      @PathVariable("id") final String id, @RequestBody final WidgetUpdateRequest widgetUpdate);

  /**
   * {@code DELETE /widgets/:id}. <br>
   * Removes existing widget by provided ID.
   *
   * @param id ID to look for widget to remove.
   * @return Empty response.
   */
  @DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
  ResponseEntity<?> deleteWidget(@PathVariable("id") final String id);
}
