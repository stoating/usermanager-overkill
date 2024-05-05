(ns web.views.home
  (:require [rum.core :as rum]
            [web.views.-default :as default]))


(println "in ns:" (str *ns*))


(def layout
  (rum/render-static-markup
   (default/page
    {}
    [:body
     [:h1.text-blue-700 "Hello, worlda"]
     [:div {:id "app"}]])))