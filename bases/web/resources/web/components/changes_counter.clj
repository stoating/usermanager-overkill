(ns web.components.changes-counter
  (:require [portal.api :as p]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn change-counter [changes-count]
  [:div {:id "changes-id"}
   (str "Your have made " changes-count " change(s) since the last reset")])


(defn changes-reset [req]
  (let [state (get-in req [:app :state])]
    (swap! state assoc :counter 0)
    (->> (get @state :counter)
         change-counter
         util/to-html)))


(defn component [req]
  (let [state (get-in req [:app :state])]
    (->> (get @state :counter)
         change-counter)))