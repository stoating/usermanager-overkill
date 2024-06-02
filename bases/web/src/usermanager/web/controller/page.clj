(ns usermanager.web.controller.page
  (:require [web.layouts.default :as d]
            [rum.core :as rum]
            [ring.util.response :as rr]))


(println "in ns:" (str *ns*))


(defn render-page [req]
  (let [body (get-in req [:app :html :body])
        hiccup (d/wrap-html-body req (body req))
        html (str "<!DOCTYPE html>\n" (rum/render-static-markup hiccup))]
    (rr/response html)))