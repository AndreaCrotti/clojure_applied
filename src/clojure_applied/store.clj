(ns clojure-applied.store)

(defn go-shopping-naive
  "Retturns a list of items purchased"
  [shopping-list]
  (loop [[item & items] shopping-list
         cart []]
    (if item
      (recur items (conj cart item)) ; the recur here just goes back to the loop changing the arguments
      cart)))

;; atom is a synchronized construct, and is also independent from anything else
;; and finally it updates immediately with no delays

(declare no-negative-values?)

(def inventory (atom {} :validator no-negative-values?))

(defn no-negative-values?
  "Check values of a map for a negative value"
  [m]
  (let [some-neg (not-any? neg? (vals m))]
    (when some-neg
      (println "Negative values ahead!!"))
    some-neg))

(defn in-stock?
  "Check if an item is in stock"
  [item]
  (let [cnt (if (contains? @inventory item) (item @inventory) 0)]
    (and (pos? cnt))))

(defn init
  "Set up store with inventory"
  [items]
  (reset! inventory items))

(defn grab
  "Grab an item from the shelves"
  [item]
  (if (in-stock? item)
    (swap! inventory update-in [item] dec)))

(defn stock
  "Stock an item on the shelves"
  [item]
  (if (in-stock? item)
    (swap! inventory update-in [item] inc)
    (swap! inventory #(assoc % item 1))))

;; can now write a much better go-shopping function

(defn shop-for-item
  "Shop for an item and return an updated cart"
  [cart item]
  (if (grab item)
    (conj cart item))
  )

(defn go-shopping
  "Returns a list of items purchased"
  [shopping-list]
  (reduce shop-for-item [] shopping-list))
