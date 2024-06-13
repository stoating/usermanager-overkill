(ns usermanager.web.controller.util
  (:require [camel-snake-kebab.core :as csk]
            [rum.core :as rum]
            [ring.util.response :as rr]))


(println "in ns:" (str *ns*))


(defn map-keys [f m]
  (->> (map (fn [[k v]] [(f k) v]) m)
       (into {})))


(defn kebabify
  [m]
  (map-keys csk/->kebab-case-keyword m))


(defn hiccup->html-resp [hiccup]
  (-> hiccup
      rum/render-static-markup
      str
      rr/response))