(ns web.views.user.list
  (:require [portal.api :as p]
            [web.components.navbar :as navbar]))

(println "in ns:" (str *ns*))
(add-tap #'p/submit)

(def user-xt-id "123")

(defn body [_]
  (fn [_]
    [:div {:class ["p-3" "mx-auto" "max-w-screen-sm" "w-full"]}
     navbar/navbar
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
  (-> req
      (assoc-in [:app :params :message]
                [:div
                 [:h1 "You're on the users page"]])
      (assoc-in [:app :html :body] (body req))))