package com.aklimenko.miro.performance;

import com.aklimenko.miro.concurrent.ConcurrentAccessLocker;
import com.aklimenko.miro.model.widget.WidgetCreateRequest;
import com.aklimenko.miro.persistence.WidgetRepositoryImpl;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH Benchmark to measure performance of widget repository with different concurrent access
 * mechanisms.
 */
public class ConcurrentAccessLockerBenchmark {

  @State(Scope.Benchmark)
  public static class SharedState {
    public ConcurrentAccessLocker lock;
    public WidgetRepositoryImpl repository;
    public List<Integer> consumeInts;
    public List<Integer> produceInts;

    @Param({
      "com.aklimenko.miro.concurrent.SynchronizedAccessLocker",
      "com.aklimenko.miro.concurrent.ReadWriteLockAccessLocker",
      "com.aklimenko.miro.concurrent.StampedLockAccessLocker"
    })
    public String lockClass;

    @Param({"10000:10000"})
    public String consumeProduce;

    @Setup(Level.Iteration)
    public void setUp() throws Exception {
      lock =
          (ConcurrentAccessLocker) Class.forName(lockClass).getDeclaredConstructor().newInstance();
      repository = new WidgetRepositoryImpl(lock);
      var nums = consumeProduce.split(":");
      var consumeListSize = Integer.parseInt(nums[0]);
      var produceListSize = Integer.parseInt(nums[1]);
      consumeInts = new Random().ints().limit(consumeListSize).boxed().collect(Collectors.toList());
      produceInts = new Random().ints().limit(produceListSize).boxed().collect(Collectors.toList());
      repository.cleanUp();
    }
  }

  @Benchmark
  @Group("measure")
  @GroupThreads(5)
  public void readWidget(final SharedState state, final Blackhole blackhole) {
    state.consumeInts.forEach(
        i -> {
          var widget = state.repository.readWidget(String.valueOf(i));
          blackhole.consume(widget);
        });
  }

  @Benchmark
  @Group("measure")
  public void createWidget(final SharedState state, final Blackhole blackhole) {
    state.produceInts.forEach(
        i -> {
          var widget = new WidgetCreateRequest(i, i, i, (double) i, (double) i);
          var created = state.repository.createWidget(widget);
          blackhole.consume(created);
        });
  }

  public static void main(String[] args) throws Exception {
    Options opt =
        new OptionsBuilder()
            .include(ConcurrentAccessLockerBenchmark.class.getSimpleName())
            .forks(1)
            .warmupIterations(2)
            .measurementIterations(5)
            .build();

    new Runner(opt).run();
  }
}
