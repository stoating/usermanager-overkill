(ns web.home
  (:require [hiccup2.core :as h]))


(def layout
  (h/html [:head
           [:title "Hello"]]
          [:body
           [:h1 "Hello, world ggstbfsbf"]
           [:div {:id "app"}]]))