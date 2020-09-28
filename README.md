# Miro task 
![Miro](docs/images/miro.png)

## Project setup
* `Java 11`
* `Maven 3.6.3`
* `Spring Boot 2.3.4.RELEASE`
* `JUnit 5`
* `JMH 1.25.2`

## Tests

```
$ mvn clean test integration-test
```

## Documentation

See [docs](docs/Miro_Take_Home_Test_(Java).pdf).

## Widgets API

### Widget model JSON
```
{
    "id": <string>, read-only
    "x": <integer>, required
    "y": <integer>, required
    "z": <integer>,
    "width": <double>, required
    "height": <double>, reqiured
    "lastModifiedAt": <timestamp>, read-only
}
```

### POST /widgets

Create a widget. The server generates the identifier. If a z-index is not specified, the widget moves to the foreground. If the existing
z-index is specified, then the new widget shifts all widgets with the same and
greater index upwards. See [docs](docs/Miro_Take_Home_Test_(Java).pdf) for details.

Request:
```
{
    "x": <integer>, required
    "y": <integer>, required
    "z": <integer>,
    "width": <double>, required
    "height": <double>, reqiured
}
```

Response:
```
Widget
```

### GET /widgets

Read paginated widgets ordered by z-index from smallest to largest.

Request query parameters:
* `limit: <integer>, optional` - amount of widgets in one page. Default is `10`. Max is `500`.
* `afterId: <string>, optional` - pagination token to look up widgets after.

Response:
```
[ Widget, ... ]
```
Headers:
```
Link: <ref>; rel="next"
```

### GET /widgets/:id

Read widget by provided ID.

Response:
```
Widget
```

### PUT /widgets/:id

Update widgets data. All changes to the widget occur atomically.

Request:
```
{
    "x": <integer>,
    "y": <integer>,
    "z": <integer>,
    "width": <double>,
    "height": <double>
}
```

Response:
```
Widget
```

### DELETE /widgets/:id

Delete widget by provided ID.

Response:
```
Widget
```

## Concurrent Access
Application implements a separate mechanism to ensure all shared resources in widgets repository is safe to access and modify concurrently. 

There is a defined `ConcurrentAccessLocker` interface with 3 different implementations based on 3 different synchronization mechanisms:
* `synchronized` keyword
* `ReadWriteReentrantLock`
* `StampedLock`

In order to run application with one or the other syncrhonization mechanism an application property `concurrent.acccesslocker` has to be defined with value: `synchronized`, `readWriteLock` or `stampedLock` where `stampedLock` is used by default when property is not specified.

Below are the results of performance measurements made with `JMH` for one of the specific scenarios:
```
# JMH version: 1.25.2
# VM version: JDK 11.0.8, OpenJDK 64-Bit Server VM, 11.0.8+10
# VM invoker: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/bin/java
# VM options: -javaagent:/Users/akli/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/202.6948.69/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64353:/Users/akli/Library/Application Support/JetBrains/Toolbox/apps/IDEA-U/ch-0/202.6948.69/IntelliJ IDEA.app/Contents/bin -Dfile.encoding=UTF-8
# Warmup: 2 iterations, 10 s each
# Measurement: 5 iterations, 10 s each
# Timeout: 10 min per iteration
# Threads: 6 threads (1 group; 5x "consume", 1x "produce" in each group), will synchronize iterations
# Benchmark mode: Throughput, ops/time

Benchmark                                        (consumeProduce)                                              (lockClass)   Mode  Cnt    Score    Error  Units
ConcurrentAccessLockerBenchmark.measure             100000:100000   com.aklimenko.miro.concurrent.SynchronizedAccessLocker  thrpt    5   13.108 ± 15.150  ops/s
ConcurrentAccessLockerBenchmark.measure:consume     100000:100000   com.aklimenko.miro.concurrent.SynchronizedAccessLocker  thrpt    5   11.520 ± 14.823  ops/s
ConcurrentAccessLockerBenchmark.measure:produce     100000:100000   com.aklimenko.miro.concurrent.SynchronizedAccessLocker  thrpt    5    1.588 ±  1.286  ops/s
ConcurrentAccessLockerBenchmark.measure             100000:100000  com.aklimenko.miro.concurrent.ReadWriteLockAccessLocker  thrpt    5   22.891 ± 10.851  ops/s
ConcurrentAccessLockerBenchmark.measure:consume     100000:100000  com.aklimenko.miro.concurrent.ReadWriteLockAccessLocker  thrpt    5   14.544 ±  5.778  ops/s
ConcurrentAccessLockerBenchmark.measure:produce     100000:100000  com.aklimenko.miro.concurrent.ReadWriteLockAccessLocker  thrpt    5    8.346 ±  5.084  ops/s
ConcurrentAccessLockerBenchmark.measure             100000:100000    com.aklimenko.miro.concurrent.StampedLockAccessLocker  thrpt    5  105.310 ± 27.526  ops/s
ConcurrentAccessLockerBenchmark.measure:consume     100000:100000    com.aklimenko.miro.concurrent.StampedLockAccessLocker  thrpt    5   80.004 ± 19.542  ops/s
ConcurrentAccessLockerBenchmark.measure:produce     100000:100000    com.aklimenko.miro.concurrent.StampedLockAccessLocker  thrpt    5   25.306 ±  8.009  ops/s
```

See [ConcurrentAccessLockerBenchmark](/src/test/java/com/aklimenko/miro/performance/ConcurrentAccessLockerBenchmark.java) for the reference.

## Rate Limit Service

Application implements rate limiting functionality with fixed window algorithm. Fixed window algorithms use a fixed rate to track the rate of requests using a simple incremental counter. The window is defined for a set number of seconds, like 3600 for one hour, for example. If the counter exceeds the limit for the set duration, the additional requests will be discarded.

Fixed window rate limit algorithm has its own advantages and disadvantages.

Advantages:
* Easy to implement
* Ensures more recent requests gets processed without being starved by old requests

Disadvantages:
* A single burst of traffic that occurs near the boundary of a window can result in twice the rate of requests being processed
* If many consumers wait for a reset window, for example at the top of the hour, then they may hammer the API at the same time

Rate limit service allows specifying both limitation for specific endpoint operations and global limitation for the rest of rate limited endpoints.

## Rate Limit Rule API

API to manage system rate limit rule parameters. Should be available only to system administrators. 

### RateLimitRule model JSON
```
{
    "windowsSizeMS": <integer>, required
    "limitGlobal": <integer>, reqired
    "limitListWidgets": <integer>, reqired
    "limitReadWidget": <integer>, reqired
    "limitCreateWidget": <integer>, reqired
    "limitUpdateWidget": <integer>, reqired
    "limitDeleteWidget": <integer>, reqired
}
```

### GET /rateLimitRule

Read rate limit rule.

Response:
```
RateLimitRule
```

### PUT /rateLimitRule

Update rate limit rule parameters.

Request:
```
{
    "windowsSizeMS": <integer>, optional
    "limitGlobal": <integer>, optional
    "limitListWidgets": <integer>, optional
    "limitReadWidget": <integer>, optional
    "limitCreateWidget": <integer>, optional
    "limitUpdateWidget": <integer>, optional
    "limitDeleteWidget": <integer>, optional
}
```

Response:
```
RateLimitRule
```