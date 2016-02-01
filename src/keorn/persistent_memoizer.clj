(ns keorn.persistent-memoizer
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def memoizer-server {:pool {} :spec {}})
(defmacro wcar* [& body] `(car/wcar memoizer-server ~@body))

(defn redis-memoize
  "Memoize to default Redis server. Numbers returned as strings."
  [f]
  (fn [& args]
    (let [key-hash (hash [f args])]
      (or (wcar* (car/get key-hash))
          (let [ret (apply f args)]
            (wcar* (car/set key-hash ret))
            ret)))))
