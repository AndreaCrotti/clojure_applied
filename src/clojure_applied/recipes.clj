(ns clojure-applied.recipes
  (:require [schema.core :as s]
            [clojure-applied.moneys :as moneys]))

(declare +$)
(def USD (moneys/->Currency 100 "USD" "us dollars"))
(def zero-dollars (moneys/->Money 0 USD))

(def pizza  (map->Recipe {:name "pizza"
                          :ingredients [(map->Ingredient {:name "pasta"
                                                          :quantity 100
                                                          :unit :kg})
                                        (map->Ingredient {:name "tomato"
                                                          :quantity 300
                                                          :unit :ml})]}))

(def simple-store
  (map->Store {:name "vivo"
               :inventary {"pasta" 1
                           "totato" 100}}))

(s/defrecord Ingredient
    [name  :- s/Str
     quantity :- s/Int
     unit :- s/Keyword])

(s/defrecord Recipe
    [name :- s/Str
     ingredients :- [Ingredient]])

(s/defrecord Store
    [name :- s/Str
     inventary :- {s/Str s/Int}]) ; make money also with schema

(defn cost-of [store ingredient]
  (let [usd (moneys/->Currency 100 "USD" "usd")]
    (moneys/->Money (rand-int 10) usd)))

;; (cost-of (->Store "store") (->Ingredient "name" 1 :kg))

;; define how the dispatch function should look like
;; here the value we dispatch with is the class of the entity itself
;; interestingly
(defmulti cost (fn [entity store] (class entity)))
(defmethod cost Recipe [recipe store]
  (reduce moneys/+$ zero-dollars
          (map #(cost % store) (:ingredients recipe))))

(defmethod cost Ingredient [ingredient store]
  (get (:inventary store) (:name ingredient)))

(cost pizza simple-store)

(s/check Ingredient
         (->Ingredient "pasta" 1 :kg))


(s/defn add-ingredients :- Recipe
  [recipe :- Recipe & ingredients :- [Ingredient]]
  (update-in recipe [:ingredients] into ingredients))


;; now use protocols to improve the code a bit
(defprotocol Cost
  (cost-proto [entity store]))

(extend-protocol Cost
  Recipe
  (cost-proto [recipe store]
    (reduce moneys/+$ zero-dollars
            (map #(cost-proto % store) (:ingredients recipe))))
  Ingredient
  (cost-proto [ingredient store]
    (cost-of store ingredient)))


(cost-proto pizza simple-store)
