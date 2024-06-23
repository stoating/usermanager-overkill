(ns web.views.home
  (:require [portal.api :as p]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn body [req]
  (let [message (get-in req [:app :params :message])]
    message))


(defn prepare-req [req]
  (-> req
      (assoc-in [:app :params :message]
                [:<>
                 [:h1 {:class ["py-3" "font-bold"]}
                  "Welcome to the User Manager!"]
                 [:p "This uses (just) Aero, Beholder, Camel-Snake-Kebab, Integrant, Malli, Polylith, Portal, Reitit, Rum, XTDB, Babashka, Flow-storm, HTMX, Tailwind, Docker, Docker-Compose, and Devcontainers."]])
      (assoc-in [:app :html :body] body)))