(ns clojure-applied.processing)

(defn orbital-period [x]
  (* Math/PI x))

(defn orbital-transformation [star]
  (map #(orbital-period % (:mass star))))

(def planets [{:name "earth" :mass 10}
              {:name "pluto" :mass 100}])

