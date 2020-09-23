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
package com.aklimenko.miro.service.concurrent;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/** {@link ConcurrentAccessLocker} implementation based on {@link StampedLockAccessLocker}. */
@Service("stampedLock")
public class StampedLockAccessLocker implements ConcurrentAccessLocker {
  private static final int OPTIMISTIC_READ_RETRIES = 10;

  private final StampedLock lock = new StampedLock();

  /**
   * Tries read data optimistically (without acquiring read lock) and falls back into read lock
   * synchronization after specified amount attempts.
   *
   * @param readSupplier Read data supplier.
   * @param retries Amount of optimistic read attempts.
   * @return Data read from shared resources.
   */
  private <RESULT> RESULT readOptimisticallyWithRetries(
      final Supplier<RESULT> readSupplier, int retries) {
    long stamp = lock.tryOptimisticRead();
    RESULT result = readSupplier.get();
    if (!lock.validate(stamp)) {
      if (retries > 0) {
        return readOptimisticallyWithRetries(readSupplier, retries - 1);
      }

      stamp = lock.readLock();
      try {
        result = readSupplier.get();
      } finally {
        lock.unlockRead(stamp);
      }
    }

    return result;
  }

  @Override
  public <RESULT> RESULT read(final Supplier<RESULT> readSupplier) {
    return readOptimisticallyWithRetries(readSupplier, OPTIMISTIC_READ_RETRIES);
  }

  @Override
  public <RESULT> RESULT write(final Supplier<RESULT> writeSupplier) {
    long stamp = lock.writeLock();

    try {
      return writeSupplier.get();
    } finally {
      lock.unlockWrite(stamp);
    }
  }

  /**
   * In optimistic scenario reads state from shared resources with read lock and converts this lock
   * to write lock for subsequent write. If conversion fails falls back to acquiring write lock and
   * rereads state and executing subsequent write.
   *
   * @param stateReader Read state supplier.
   * @param writeFunc Write data based on read state.
   * @return Data written to shared resources.
   */
  @Override
  public <RESULT, STATE> RESULT readStateAndWrite(
      final Supplier<STATE> stateReader, final Function<STATE, RESULT> writeFunc) {
    RESULT result = null;
    long stamp = lock.readLock();
    try {
      while (true) {
        // reading state with read lock first
        STATE state = stateReader.get();

        long writeStamp = lock.tryConvertToWriteLock(stamp);
        if (writeStamp != 0) {
          // in optimistic scenario read lock is converted to write lock and write is executed
          stamp = writeStamp;
          result = writeFunc.apply(state);
          break;
        } else {
          // in pessimistic scenario releasing read lock and acquiring write lock
          lock.unlockRead(stamp);
          stamp = lock.writeLock();
        }
      }
    } finally {
      lock.unlock(stamp);
    }

    return result;
  }
}
