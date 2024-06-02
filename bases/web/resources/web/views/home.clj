(ns web.views.home
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.web.controller.page :as page]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def changes
  "Count the number of changes (since the last reload)."
  (atom 5))


(defn my-changes [changes]
  [:div {:id "changes-id"}
   (str "Your have made " changes " change(s) since the last reset")])


(defn changes-reset [_]
  (reset! changes 0)
  (page/to-html (my-changes @changes)))


(def my-message
  [:button {:hx-get (get rs/rs :home-message-toggle)
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "click me to toggle message"])


(defn message-toggle [_]
  (-> my-message
      (assoc-in [1 :hx-get] (get rs/rs :home-message-toggle-reset))
      (assoc 2 "click me to reset the message")
      page/to-html))


(defn message-toggle-reset [_]
  (page/to-html my-message))


(defn body [_]
  (fn [req]
    (let [message (get-in req [:app :params :message])]
      [:<>
       message
       [:br]
       (my-changes @changes)
       [:br]
       my-message])))


(defn home [req]
  (-> req
      (assoc-in [:app :params :changes] @changes)
      (assoc-in [:app :params :message]
                [:div
                 [:h1 "Welcome to the User Manager"]
                 [:p "This uses just Aero, Beholder, Integrant, Polylith, Portal, Reitit, Rum, XTDB, Babashka, HTMX, Tailwind, Docker, and Devcontainers."]])
      (assoc-in [:app :html :body] (body req))))