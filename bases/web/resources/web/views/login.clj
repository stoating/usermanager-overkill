(ns web.views.login
  (:require [rum.core :as rum]
            [web.views.-default :as default]))


(println "in ns:" (str *ns*))


(def layout
  (rum/render-static-markup
   (default/page
    {}
    [:body
     [:h1.text-blue-400 "im the login"]
     [:div {:id "app"}]
     [:a {:href "/"} "home"]])))

(println "end ns:" (str *ns*))