(ns clojure-applied.colls
  (import [clojure.lang Counted Indexed ILookup Seqable])
  (:require [medley.core :as medley]))

;; keep in mind of this difference
(= (vec (conj '(1 2) 3))  (conj [1 2] 3))

(def authors [{:first "andrea" :last "crotti"}
              {:first "lucia" :last "rov"}
              {:first "enzo" :last "crotti"}])

(defn compare-people [fst snd]
  (compare (:last fst) (:last snd)))
  
(defn smart-compare [a1 a2]
  (letfn [(project-author [author]
            ((juxt :last :first) author))]
    (compare (project-author a1) (project-author a2))))


(sorted-set-by smart-compare
               {:first "andrea" :last "crotti"}
               {:first "enzo" :last "crotti"}
               )

;; use a persistent queue when we have a FIFO scenario
(peek  (conj clojure.lang.PersistentQueue/EMPTY 200))

;; on using transientes
(defn import-catalog [data]
  (reduce #(conj %1 %2) [] data))

(defn import-catalog-trans [data]
  (persistent!
   (reduce #(conj! %1 %2) (transient []) data)))


(defn keywordize-entity [entity]
  (medley/map-keys keyword entity))

(keywordize-entity {"name" 1
                    "last" 2})


(deftype Pair [a b]
  Seqable
  (seq [_] (seq [a b]))

  Counted
  (count [_] 2)

  Indexed
  (nth [_ i]
    (case i
      0 a
      1 b
      (throw (IllegalArgumentException.))))

  ILookup
  (valAt [_ k _]
    (case k
      0 a
      1 b
      (throw (IllegalArgumentException.))))
  (valAt [this k] (.valAt this k nil)))

(defmethod print-method Pair
  [pair w]
  (.write w "#ch2.pair.Pair")
  (print-method (vec (seq pair)) w))
