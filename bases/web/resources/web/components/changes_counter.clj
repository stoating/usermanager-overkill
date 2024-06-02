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
    (let [counter (get @state :counter)]
      (util/to-html (change-counter counter)))))


(defn component [req]
  (let [state (get-in req [:app :state])
        counter (get @state :counter)]
    (change-counter counter)))