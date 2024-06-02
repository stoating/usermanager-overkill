(ns web.views.user.list
  (:require [portal.api :as p]))

(println "in ns:" (str *ns*))
(add-tap #'p/submit)

(def user-xt-id "123")

(defn body [_]
  (fn [_]
    [:<>
     [:table
      [:thead
       [:tr
        [:th "Name"]
        [:th "Email"]
        [:th "Department"]
        [:th "Delete"]]]
      [:tbody
       [:tr
        [:td "John Doe"]
        [:td "Email"]
        [:td "Name"]
        [:td [:a {:href (str "/user/delete/" user-xt-id)} "Delete"]]]]]]))

(defn user-list [req]
  (assoc-in req [:app :html :body] (body req)))