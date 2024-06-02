(ns web.views.home
  (:require [portal.api :as p]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn body [req]
  (let [message (get-in req [:app :params :message])]
    [:<>
     message]))


(defn home [req]
  (-> req
      (assoc-in [:app :params :message]
                [:<>
                 [:h1 "Welcome to the User Manager"]
                 [:p "This uses just Aero, Beholder, Integrant, Polylith, Portal, Reitit, Rum, XTDB, Babashka, HTMX, Tailwind, Docker, and Devcontainers."]])
      (assoc-in [:app :html :body] body)))