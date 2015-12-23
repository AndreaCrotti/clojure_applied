(ns clojure-applied.moneys-test
  (:require [clojure-applied.moneys :as moneys]
            [clojure.test.check.generators :as gens]
            [clojure.test.check :refer [quick-check]]
            [clojure.test :refer :all]))


(def USD (moneys/map->Currency {:divisor 100
                                :sym "USD"
                                :desc "US dollars"}))

(def EURO (moneys/map->Currency {:divisor 100
                                 :sym "EUR"
                                 :desc "Euro"}))

;;TODO: move this out
(defn make-money
  ([] (make-money 0))
  ([amount] (make-money amount USD))
  ([amount currency] (moneys/->Money amount currency)))

(deftest money-checks
  (testing "Check money comparison"
    (is (= (make-money 10 EURO) (make-money 10 EURO)))))
