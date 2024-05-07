(ns web.views.home
  (:require [rum.core :as rum]
            [web.views.-default :as default]
            [clojure.string :as str]))


(println "in ns:" (str *ns*))

(defn tw
  [classes]
  (->> (flatten classes)
       (remove nil?)
       (map name)
       (sort)
       (str/join " ")))

(def tw-input
  ["text-6xl"])

(def layout
  (rum/render-static-markup
   (default/page
    {}
    [:body
     [:h1 {:class (tw [tw-input])}"Hello worlda"]
     [:div {:id "app"}]
     [:a {:href "/login"} "login"]])))