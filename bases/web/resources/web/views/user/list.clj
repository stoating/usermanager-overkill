(ns web.views.user.list
  (:require #_[rum.core :as rum]))

(println "in ns:" (str *ns*))

#_(def user-xt-id "123")

#_(defn user-list []
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

(println "end ns:" (str *ns*))