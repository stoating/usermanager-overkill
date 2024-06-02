(ns web.views.home
  (:require [portal.api :as p]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def changes
  "Count the number of changes (since the last reload)."
  (atom 5))


(defn change-counter [changes]
  [:div {:id "changes-id"}
   (str "Your have made " changes " change(s) since the last reset")])


(defn changes-reset [_]
  (reset! changes 0)
  (util/to-html (change-counter @changes)))


(defn body [_]
  (fn [req]
    (let [message (get-in req [:app :params :message])]
      [:<>
       message
       [:br]
       (change-counter @changes)])))


(defn home [req]
  (-> req
      (assoc-in [:app :params :changes] @changes)
      (assoc-in [:app :params :message]
                [:<>
                 [:h1 "Welcome to the User Manager"]
                 [:p "This uses just Aero, Beholder, Integrant, Polylith, Portal, Reitit, Rum, XTDB, Babashka, HTMX, Tailwind, Docker, and Devcontainers."]])
      (assoc-in [:app :html :body] (body req))))