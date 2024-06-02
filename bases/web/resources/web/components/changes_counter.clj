(ns web.components.changes-counter
  (:require [portal.api :as p]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn change-counter [changes]
  [:div {:id "changes-id"}
   (str "Your have made " changes " change(s) since the last reset")])


(defn changes-reset [req]
  (reset! (get-in req [:app :state]) 0)
  (util/to-html (change-counter @(get-in req [:app :state]))))


(defn component [& state]
  [:<>
   [:br]
   (change-counter @(first state))])