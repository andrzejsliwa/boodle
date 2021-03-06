(ns boodle.utils.numbers-test
  (:require [boodle.utils.numbers :as n]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))

(defn converted-amount?
  "Check if `s` contains \",\"."
  [s]
  (not (nil? (re-matches #"\d+,\d+" s))))

(def gen-amount
  (gen/double* {:infinite? false :NaN? false :min 1 :max 10000}))

(defspec converted-amount-always-contains-comma
  100
  (prop/for-all [amount (gen/fmap n/en->ita gen-amount)]
    (converted-amount? amount)))

(def gen-expense
  (gen/hash-map
   :item gen/string-alphanumeric
   :amount gen-amount))

(defspec expense-must-contain-converted-amount
  100
  (prop/for-all [expense gen-expense]
    (-> expense
        (n/convert-amount :amount)
        (:amount)
        converted-amount?)))

(defspec amount-nil-safely-returns-zero
  100
  (prop/for-all [amount gen/double]
    (not (nil? (n/or-zero amount)))))
