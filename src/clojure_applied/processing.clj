(ns clojure-applied.processing
  (:require [schema.core :as s]
            [schema.experimental.generators :as g]
            [medley.core :as medley]))

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
  ;; important to note that the transformer function
  ;; has now been extracted (map :moons) and could be reused
  ;; and also applying a transducer does everything in a single
  ;; pass, it does not need to generate the intermediate result
  ;; first
  (transduce (map :moons) + 0 planets))

(total-moons planets)

(medley/map-vals (fn [n] (+ n 1)) {1 1 2 2})

(defn find-planet [planets pname]
  (reduce
   ;; this anonymous function is the reducing function
   ;; it takes the accumulated value and a new collection
   ;; element being traversed
   (fn [_ planet]
     (when (= pname (:name planet))
       (reduced planet)))
   planets))

(find-planet planets "earth")

;; (defrecord Planet [name moons])
(s/defrecord Planet
    [name :- s/Str
     moons :- s/Int])

(defn planet? [entity]
  (instance? Planet entity))

(planet? (->Planet "earth" 23))

(defn total-moons [entities]
  (reduce + 0
          (map :moons
               (filter planet?
                       entities))))
;; use magic generator
(def ten-planets (for [i (range 10)]
                   (g/generate Planet)))

(total-moons ten-planets)

;; rewrite total moons to use the threading macro
(defn total-moons-threading [entities]
  (->> entities
       (filter planet?)
       (map :moons)
       (reduce + 0)))

(total-moons-threading ten-planets)

;; use composition now instead
(def moons-transform
  ;; this now is only the transformation function,
  ;; that does not take care of actually reducing anything
  (comp (filter planet?) (map :moons)))

(defn total-moons-red [entities]
  (transduce moons-transform + 0 entities))

(total-moons-red planets)

;; the biggest change of transducers is the fact that we compose
;; transformations making them more efficient and able to do all
;; in one go

;; copy of the time macro but removing the print of the final resulting
;; expression
(defmacro mytime
  "Evaluates expr and prints the time it took.  Returns the value of
 expr."
  [expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr]
     (prn (str "Elapsed time: " (/ (double (- (. System (nanoTime)) start#)) 1000000.0) " msecs"))))

(defn has-moons? [planet]
  (pos? (:moons planet)))

(def moons-transform-with-has-moons
  (comp (filter has-moons?) (map :moons)))

(filter has-moons? ten-planets)


;; the final workflow to generate a result is something like
;; - figure out the question you're trying to ask
;; - filter the data
;; - transform to the desired form
;; - reduce to get the final answer
