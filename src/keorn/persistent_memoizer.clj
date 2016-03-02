(ns keorn.persistent-memoizer
  (:require [taoensso.carmine :as car :refer [wcar]]))

(def memoizer-server {:pool {} :spec {}})

(defn redis-memoize-fn
  "Memoize to default Redis server. Numbers returned as strings."
  [f-name f & seed]
  (fn [& args]
    (let [key-hash (hash (str f-name args seed))]
      (or (car/wcar memoizer-server (car/get key-hash))
          (let [ret (apply f args)]
            (car/wcar memoizer-server (car/set key-hash ret))
            ret)))))

(defmacro redis-memoize
  "Find the name of function and memoize."
  [f & seed]
  `(-> ~f var meta :name str (~redis-memoize-fn ~f ~seed)))
