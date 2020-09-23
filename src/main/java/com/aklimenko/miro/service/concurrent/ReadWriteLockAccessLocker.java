package com.aklimenko.miro.service.concurrent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * {@link ConcurrentAccessLocker} implementation based on {@link ReentrantReadWriteLock}.
 */
@Service("readWriteLock")
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
  public <RESULT, STATE> RESULT readStateAndWrite(Supplier<STATE> stateReader, Function<STATE, RESULT> writeFunc) {
    lock.writeLock().lock();
    try {
      STATE state = stateReader.get();
      return writeFunc.apply(state);
    } finally {
      lock.writeLock().unlock();
    }
  }
}
