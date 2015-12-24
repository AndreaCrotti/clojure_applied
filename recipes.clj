(ns clojure-applied.recipes
  (:require [schema.core :as s]))

(s/defrecord Ingredient
    [name  :- s/Str
     quantity :- s/Int
     unit :- s/Keyword])

(s/defrecord Recipe
    [name :- s/Str
     ingredients :- [Ingredient]])

(s/check Ingredient
         (->Ingredient "pasta" 1 :kg))


(s/defn add-ingredients :- Recipe
  [recipe :- Recipe & ingredients :- [Ingredient]]
  (update-in recipe [:ingredients] into ingredients))
