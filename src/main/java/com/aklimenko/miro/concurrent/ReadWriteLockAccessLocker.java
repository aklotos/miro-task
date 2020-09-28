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

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/** {@link ConcurrentAccessLocker} implementation based on {@link ReentrantReadWriteLock}. */
@Service
@ConditionalOnProperty(value = "concurrent.accesslocker", havingValue = "readWriteLock")
public class ReadWriteLockAccessLocker implements ConcurrentAccessLocker {

  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Override
  public <RESULT> RESULT read(Supplier<RESULT> readSupplier) {
    lock.readLock().lock();
    try {
      return readSupplier.get();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public <RESULT> RESULT write(Supplier<RESULT> writeSupplier) {
    lock.writeLock().lock();
    try {
      return writeSupplier.get();
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public <RESULT, STATE> RESULT readStateAndWrite(
      Supplier<STATE> stateReader, Function<STATE, RESULT> writeFunc) {
    lock.writeLock().lock();
    try {
      STATE state = stateReader.get();
      return writeFunc.apply(state);
    } finally {
      lock.writeLock().unlock();
    }
  }
}
