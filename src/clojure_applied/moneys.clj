(ns clojure-applied.moneys)

(declare validate-same-currency)

(defrecord Currency [divisor sym desc])

(defrecord Money [amount ^Currency currency]
  java.lang.Comparable
  (compareTo [m1 m2]
    (validate-same-currency m1 m2)
    (compare (:amount m1) (:amount m2))))

(defn- validate-same-currency
  [m1 m2]
  (or (= (:currency m1) (:currency m2))
      (throw
       (ex-info "Currencies do not match"
                {:m1 m1 :m2 m2}))))

(defn =$
  ([m1] true)
  ([m1 m2] (zero? (.compareTo m1 m2)))
  ([m1 m2 & moneys]
   (every? zero? (map #(.compareTo m1 %) (conj moneys m2)))))

