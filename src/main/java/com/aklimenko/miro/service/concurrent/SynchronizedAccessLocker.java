package com.aklimenko.miro.service.concurrent;

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
