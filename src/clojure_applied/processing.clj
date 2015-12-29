(ns clojure-applied.processing
  (:require [schema.core :as s]))

(defn orbital-period [x _]
  (* Math/PI x))

(defn orbital-transformation [star]
  (map #(orbital-period % (:mass star))))

(def planets [{:name "earth" :mass 10 :moons 1}
              {:name "pluto" :mass 100 :moons 3}])


(defn orbital-periods
  [planets star]
  (into [] (orbital-transformation star) planets))


;; transducer session
(def numbers (range 10))

(defn myinc [n]
  (+ n 1))

(def mytransducer
  (map myinc))

;; various examples to fill in the output result
(sequence mytransducer numbers)
(into [] mytransducer numbers)
(into #{} mytransducer numbers)

;; have a look at reducers now
(defn total-moons [planets]
  (reduce + 0 (map :moons planets)))

(total-moons planets)

;; same thing jnow now with a transducer
(defn total-mooons
  [planets]
  (transduce (map :moons) + 0 planets))

(total-moons planets)
