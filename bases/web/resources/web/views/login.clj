(ns web.views.login
  (:require [rum.core :as rum]))


(println "in ns:" (str *ns*))


(def layout
  (rum/render-static-markup
   [:head
    [:title "Hello"]
    [:link {:rel "stylesheet", :href "assets/css/tailwind_output.css"}]
    [:body
     [:h1.text-blue-400 "im the login"]
     [:div {:id "app"}]]]))