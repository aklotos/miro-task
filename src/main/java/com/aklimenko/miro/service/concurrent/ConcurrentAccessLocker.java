package com.aklimenko.miro.service.concurrent;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents the contract of locker functionality to manage concurrent access to shared resources.
 */
public interface ConcurrentAccessLocker {

  /**
   * Reads data from the shared resources. Based on implementation it can be optimistic read with
   * retries, pessimistic read with read lock or fully synchronized access to shared resources.
   *
   * @param readSupplier Read data supplier.
   * @return Data read from shared resources.
   */
  <RESULT> RESULT read(final Supplier<RESULT> readSupplier);

  /**
   * Writes data to the shared resources. Based on implementation it can be either fully
   * synchronized or syncronized on write lock.
   *
   * @param writeSupplier Write data supplier.
   * @return Data written to shared resources.
   */
  <RESULT> RESULT write(final Supplier<RESULT> writeSupplier);

  /**
   * Reads state from shared resources and writes data to shared resources based on read state.
   * Based on implementation it can be either fully synchronized read and subsequent write or write
   * lock synchronized read and write or read lock synchronized read with an attempt to transform
   * read lock to write lock for subsequent write.
   *
   * @param stateReader Read state supplier.
   * @param writeFunc Write data based on read state.
   * @return Data written to shared resources.
   */
  <RESULT, STATE> RESULT readStateAndWrite(
      final Supplier<STATE> stateReader, final Function<STATE, RESULT> writeFunc);
}
