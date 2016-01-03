(ns clojure-applied.store-test
  (:require [clojure-applied.store :as store]
            [clojure.test :refer :all]
            [expectations :as e]))


(defn reset-inventory [f]
  (store/init {})
  (f))

(use-fixtures :each reset-inventory)

(deftest naive-test
  (testing "Naive approach"
    (is (= (store/go-shopping-naive []) []))
    (is (= (store/go-shopping-naive ["item1"]) ["item1"]))))

(deftest stock
  (testing "Add something in stock"
    (store/stock :pasta)
    (is (true? (store/in-stock? :pasta)))
    (store/grab :pasta)
    (is (false? (store/in-stock? :pasta)))))
