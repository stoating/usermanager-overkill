(ns web.components.navbar
  (:require [routes :as rs]))


(def component
  [:ul {:class ["flex" "flex-row"]}
   [:li {:class ["pr-4 py-4"]}
    [:a {:href (get rs/rs :home)} "Home"]]
   [:li {:class ["pr-4 py-4"]}
    [:a {:href (get rs/rs :user-list)
         :title "View the list of users"} "Users"]]
   [:li {:class ["pr-4 py-4"]}
    [:a {:href (get rs/rs :user-form)
         :title "Fill out form to add new user"} "Add User"]]
   [:li {:class ["pr-4 py-4"]}
    [:button {:hx-get (get rs/rs :default-changes-reset)
              :hx-trigger "click"
              :hx-target "#changes-id"
              :hx-swap "outerHTML"
              :title "Resets change tracking"}
     "Reset"]]])