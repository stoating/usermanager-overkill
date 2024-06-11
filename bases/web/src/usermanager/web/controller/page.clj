(ns usermanager.web.controller.page
  (:require [web.layouts.default :as d]
            [rum.core :as rum]
            [ring.util.response :as rr]))


(println "in ns:" (str *ns*))


(defn render-page [resp]
  (let [body (get-in resp [:app :html :body])
        hiccup (d/wrap-html-body resp (body resp))
        html (str "<!DOCTYPE html>\n" (rum/render-static-markup hiccup))]
    (rr/response html)))