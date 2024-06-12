(ns web.components.changes-counter
  (:require [portal.api :as p]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn change-counter [changes-count]
  [:div {:id "changes-id"}
   [:p "Your have made " [:span {:class ["text-lg" "font-bold"]} changes-count] " change(s) since the last reset"]])


(defn changes-inc [req]
  (let [state (get-in req [:app :state])]
    (swap! state assoc :counter (inc (get @state :counter)))
    (->> (get @state :counter)
         change-counter
         util/hiccup->html-resp)))


(defn changes-reset [req]
  (let [state (get-in req [:app :state])]
    (swap! state assoc :counter 0)
    (->> (get @state :counter)
         change-counter
         util/hiccup->html-resp)))


(defn component [req]
  (let [state (get-in req [:app :state])]
    (->> (get @state :counter)
         change-counter)))