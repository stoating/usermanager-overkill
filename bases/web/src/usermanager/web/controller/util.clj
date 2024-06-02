(ns usermanager.web.controller.util
  (:require [rum.core :as rum]
            [ring.util.response :as rr]))


(println "in ns:" (str *ns*))


(defn to-html [hiccup]
  (-> hiccup
      rum/render-static-markup
      str
      rr/response))