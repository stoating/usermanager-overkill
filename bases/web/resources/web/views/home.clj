(ns web.views.home
  (:require [hiccup2.core :as h]))


(println "in ns:" (str *ns*))


(def layout
  (h/html [:head
           [:title "Hello"]
           [:link {:rel "stylesheet", :href "output.css"}]]
          [:body
           [:h1 "Hello, worald"]
           [:div.text-blue-500 {:id "app"}
            "honssk honk"]]))