# Miro task 
![Miro](docs/images/miro.png)

## Project setup
* `Java 11`
* `Maven 3.6.3`
* `Spring Boot 2.3.4.RELEASE`
* `JUnit 5`

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