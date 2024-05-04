(ns web.home
  (:require [hiccup2.core :as h]))


(println "in ns:" (str *ns*))


(def layout
  (h/html [:head
           [:title "Hello"]]
          [:body
           [:h1 "Hello, world sfffpbf"]
           [:div {:id "app"}]]))