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
package com.aklimenko.miro.exception;

/**
 * Exception to indicate that z-index limit is exceeded.
 * Mapped to HTTP 409 Conflict response.
 */
public class ZIndexLimitExceededException extends RuntimeException {

  public ZIndexLimitExceededException(final String widgetId) {
    super("Z-index limit exceeded. Can not place widget on top of widget [id=" + widgetId + "].");
  }
}
