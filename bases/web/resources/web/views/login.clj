(ns web.views.login
  (:require [hiccup2.core :as h]))


(println "in ns:" (str *ns*))


(def layout
  (h/html [:head
           [:title "Hello"]]
          [:body
           [:h1 "im the login"]
           [:div {:id "app"}]]))