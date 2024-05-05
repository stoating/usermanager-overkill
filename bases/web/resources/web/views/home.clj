(ns web.views.home
  (:require [hiccup2.core :as h]))


(println "in ns:" (str *ns*))


(def layout
  (h/html [:head
           [:title "Hello"]
           [:link {:rel "stylesheet", :href "assets/css/tailwind_output.css"}]]
          [:body
           [:h1.text-blue-700 "Hello, wossss"]
           [:div.text-blue-400 {:id "app"}
            "honssk honk"]]))