(ns web.views.user.form
  (:require [rum.core :as rum]))

(println "in ns:" (str *ns*))

(defn form-template []
  (rum/render-static-markup
   [:form {:method "post"
           :action "/user/save"}]

   [:div
    [:label]
    [:input]]

   [:div
    [:label]
    [:input]]

   [:div
    [:label]
    [:input]]

   [:div
    [:label]
    [:select {:name "department_id"
              :id "department_id"}]]

   [:div
    [:input {:type "submin"
             :value "Save User"}]]
   ))

(println "end ns:" (str *ns*))