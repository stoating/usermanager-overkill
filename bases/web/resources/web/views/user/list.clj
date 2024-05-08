(ns web.views.user.list
  (:require [rum.core :as rum]))

(def user-xt-id "123")

(defn user-list []
  (rum/render-static-markup
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
      [:td [:a {:href (str "/user/delete/" user-xt-id)} "Delete"]]]]]))