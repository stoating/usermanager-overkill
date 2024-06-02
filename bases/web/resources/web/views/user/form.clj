(ns web.views.user.form
  (:require [portal.api :as p]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn body [_]
  [:<>
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
             :value "Save User"}]]])


(defn prepare-req [req]
  (assoc-in req [:app :html :body] body))