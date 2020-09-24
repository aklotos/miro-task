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
package com.aklimenko.miro.concurrent;

import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/** {@link ConcurrentAccessLocker} implementation based on full synchronization on the locker. */
@Service("synchronized")
public class SynchronizedAccessLocker implements ConcurrentAccessLocker {

  @Override
  public synchronized <RESULT> RESULT read(Supplier<RESULT> readSupplier) {
    return readSupplier.get();
  }

  @Override
  public synchronized <RESULT> RESULT write(Supplier<RESULT> writeSupplier) {
    return writeSupplier.get();
  }

  @Override
  public synchronized <RESULT, STATE> RESULT readStateAndWrite(
      Supplier<STATE> stateReader, Function<STATE, RESULT> writeFunc) {
    STATE state = stateReader.get();
    return writeFunc.apply(state);
  }
}
