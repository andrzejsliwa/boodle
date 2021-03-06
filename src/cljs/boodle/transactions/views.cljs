(ns boodle.transactions.views
  (:require [boodle.common :as common]
            [boodle.i18n :refer [translate]]
            [boodle.modal :as modal]
            [boodle.validation :as v]
            [re-frame.core :as rf]))

(defn amounts
  []
  (fn []
    (let [rows (rf/subscribe [:active-aim-transactions])
          target (:target (first @rows))
          tot-amount (reduce + (map :amount @rows))
          amount-left (common/format-number (- target tot-amount))]
      [:div
       [:div.row
        {:style {:text-align "center" :margin-top "-1.8em"}}
        [:div.four.columns
         {:style {:text-align "center"}}
         [:h5 (translate :it :transactions/label.target)
          [:strong {:style {:color "#718c00"}}
           (str (v/or-zero target) (translate :it :currency))]]]
        [:div.four.columns
         {:style {:text-align "center"}}
         [:h5 (translate :it :transactions/label.saved)
          [:strong {:style {:color "#f5871f"}}
           (str (v/or-zero tot-amount) (translate :it :currency))]]]
        [:div.four.columns
         {:style {:text-align "center"}}
         [:h5 (translate :it :transactions/label.left)
          [:strong {:style {:color "#c82829"}}
           (str (v/or-zero amount-left) (translate :it :currency))]]]]])))

(defn render-transaction-row
  [row]
  (when-let [amount (:amount row)]
    (let [amount-str (if (pos? amount) (str "+" amount) amount)
          color (if (pos? amount) "#718c00" "#c82829")]
      [:tr {:key (random-uuid)}
       [:td (:date row)]
       [:td (:item row)]
       [:td
        {:style {:color color}}
        [:strong (str amount-str (translate :it :currency))]]])))

(defn transactions-table
  [aim-type]
  (fn []
    (let [rows (rf/subscribe [aim-type])]
      [:table.u-full-width
       [:thead
        [:tr
         [:th (translate :it :transactions/table.date)]
         [:th (translate :it :transactions/table.item)]
         [:th (translate :it :transactions/table.amount)]]]
       [:tbody
        (doall (map render-transaction-row @rows))]])))
