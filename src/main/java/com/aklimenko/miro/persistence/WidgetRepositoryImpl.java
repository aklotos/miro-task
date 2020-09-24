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
import com.aklimenko.miro.model.widget.Widget;
import com.aklimenko.miro.model.widget.WidgetCreateRequest;
import com.aklimenko.miro.model.widget.WidgetUpdateRequest;
import com.aklimenko.miro.concurrent.ConcurrentAccessLocker;
import com.aklimenko.miro.utils.ValidationHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/** Implementation of {@link WidgetRepository} contract. */
@Repository
public class WidgetRepositoryImpl implements WidgetRepository {

  private final Map<String, Widget> widgetsById = new HashMap<>();
  private final NavigableMap<Integer, Widget> widgetsByZIndex = new TreeMap<>();

  private final ConcurrentAccessLocker accessLocker;

  private static class WidgetUpdateState {
    private final Widget updatedWidget;
    private final Integer shiftFromZ;
    private final int previousZ;

    public WidgetUpdateState(
        final Widget updatedWidget, final Integer shiftFromZ, final int previousZ) {
      this.updatedWidget = updatedWidget;
      this.shiftFromZ = shiftFromZ;
      this.previousZ = previousZ;
    }
  }

  public WidgetRepositoryImpl(@Qualifier("stampedLock") final ConcurrentAccessLocker accessLocker) {
    this.accessLocker = accessLocker;
  }

  /**
   * Retrieves next free foreground z-index. This method accesses shared resources so must be used
   * only in synchronized context.
   *
   * @return Next free z-index.
   */
  private int nextForegroundZIndex() {
    if (widgetsByZIndex.isEmpty()) {
      return 0;
    }

    final Widget topWidget = widgetsByZIndex.get(widgetsByZIndex.lastKey());
    ValidationHelper.ensureSafeToPlaceOnTopOf(topWidget);
    return topWidget.getZ() + 1;
  }

  /**
   * Shift upwards all widgets starting from specified z-index. This method accesses and modifies
   * shared resources so must be used only in synchronized context.
   *
   * @param z Z-index to start shifing from.
   */
  private void shiftUpwardsFrom(int z) {
    // ensure there is room on top of the widget in the foreground
    nextForegroundZIndex();

    final NavigableMap<Integer, Widget> widgetsToShift = widgetsByZIndex.tailMap(z, true);
    final Iterator<Integer> zIndexesToShift = widgetsToShift.navigableKeySet().iterator();

    final List<Widget> shifted = new ArrayList<>();
    while (zIndexesToShift.hasNext()) {
      int zToShift = zIndexesToShift.next();
      final Widget widgetToShift = widgetsByZIndex.get(zToShift);
      shifted.add(widgetToShift.shiftUpward());
      zIndexesToShift.remove();
    }

    shifted.forEach(
        widget -> {
          widgetsById.put(widget.getId(), widget);
          widgetsByZIndex.put(widget.getZ(), widget);
        });
  }

  @Override
  public Widget createWidget(WidgetCreateRequest widgetToCreate) {
    return accessLocker.readStateAndWrite(
        // reading state first since nextForegroundZIndex method accessing shared resources
        () -> {
          final int zIndex =
              Optional.ofNullable(widgetToCreate.getZ()).orElse(nextForegroundZIndex());
          return widgetToCreate.toNewWidget(zIndex);
        },
        (newWidget) -> {
          if (widgetsByZIndex.containsKey(newWidget.getZ())) {
            shiftUpwardsFrom(newWidget.getZ());
          }

          widgetsById.put(newWidget.getId(), newWidget);
          widgetsByZIndex.put(newWidget.getZ(), newWidget);

          return newWidget;
        });
  }

  @Override
  public Collection<Widget> listWidgets() {
    return accessLocker.read(widgetsByZIndex::values);
  }

  @Override
  public Widget readWidget(String id) {
    return accessLocker.read(
        () -> {
          final Widget foundWidget = widgetsById.get(id);
          if (foundWidget == null) {
            throw new WidgetNotFoundException(id);
          }

          return foundWidget;
        });
  }

  @Override
  public Widget updateWidget(String id, WidgetUpdateRequest widgetUpdate) {
    // z-Index is not updated
    if (widgetUpdate.getZ() == null) {
      return accessLocker.readStateAndWrite(
          // reading state to ensure widget with provided id exists
          () -> {
            final Widget widgetToUpdate = widgetsById.get(id);
            if (widgetToUpdate == null) {
              throw new WidgetNotFoundException(id);
            }
            return widgetToUpdate.updateBy(widgetUpdate);
          },
          (updatedWidget) -> {
            widgetsById.put(id, updatedWidget);
            widgetsByZIndex.put(updatedWidget.getZ(), updatedWidget);
            return updatedWidget;
          });
    }

    // z-Index is updated
    return accessLocker.readStateAndWrite(
        () -> {
          final Widget widgetToUpdate = widgetsById.get(id);
          if (widgetToUpdate == null) {
            throw new WidgetNotFoundException(id);
          }
          int previousZ = widgetToUpdate.getZ();
          Widget updatedWidget = widgetToUpdate.updateBy(widgetUpdate);

          final int z = widgetUpdate.getZ();
          final Integer zToShift = (widgetsByZIndex.containsKey(z)) ? z : null;
          return new WidgetUpdateState(updatedWidget, zToShift, previousZ);
        },
        (widgetUpdateState) -> {
          final Integer shiftFromZ = widgetUpdateState.shiftFromZ;
          if (shiftFromZ != null) {
            shiftUpwardsFrom(shiftFromZ);
          }
          widgetsByZIndex.remove(widgetUpdateState.previousZ);

          final Widget updatedWidget = widgetUpdateState.updatedWidget;
          widgetsById.put(id, updatedWidget);
          widgetsByZIndex.put(updatedWidget.getZ(), updatedWidget);
          return updatedWidget;
        });
  }

  @Override
  public void deleteWidget(String id) {
    accessLocker.readStateAndWrite(
        // reading state to ensure widget with provided id exists
        () -> {
          final Widget widgetToRemove = widgetsById.get(id);
          if (widgetToRemove == null) {
            throw new WidgetNotFoundException(id);
          }
          return widgetToRemove;
        },
        // performing widget deletion
        (widgetToRemove) -> {
          widgetsById.remove(id);
          widgetsByZIndex.remove(widgetToRemove.getZ());

          return Void.TYPE;
        });
  }

  /**
   * Cleans up repository storage. Not part of the {@link WidgetRepository} contract and used only
   * for testing purposes. Not synchronized.
   */
  public void cleanUp() {
    widgetsById.clear();
    widgetsByZIndex.clear();
  }
}
