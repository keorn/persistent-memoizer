# persistent-memoizer

A Clojure library with one function: redis-memoize. It works like clojure.core/memoize, but keeps the values across JVM restarts thanks to Redis.

Redis is handled by [Carmine](https://github.com/ptaoussanis/carmine). The memoizer requires [Redis](http://redis.io/).

The included `redis.conf` sets up Redis to function as a LRU (Least Recently Used) cache, with 1 GB memory limit.

```clojure
[com.keorn/persistent-memoizer "1.0-SNAPSHOT"]
```

## Usage

Start Redis server on at `localhost:6379`:
```redis-server redis.conf```

```clojure
(ns your.ns
  (:require [keorn.persistent-memoizer :refer [redis-memoize]]))

;; Define your computationally expensive function
(defn slow-return []
  (Thread/sleep 3000)
  [42 "cheese"])

;; Memoize it
(def fast-return (redis-memoize slow-return))

;; Call it once, wait for computation
(fast-return)

;; Call it again: returns memoized value. Persists, as long as, it is still in Redis.
(fast-return)
```

Serialization is done using [Nippy](https://github.com/ptaoussanis/nippy), types are handled as follows:

Clojure type             | Redis type
------------------------ | --------------------------
Strings                  | Redis strings
Keywords                 | Redis strings (v2+)
Simple numbers           | Redis strings
Everything else          | Auto de/serialized with Nippy

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
