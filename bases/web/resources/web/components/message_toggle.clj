(ns web.components.message-toggle
  (:require [usermanager.web.controller.util :as util]
            [routes :as rs]))

(def message
  [:button {:hx-get (get rs/rs :home-message-toggle)
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "click me to toggle message"])


(defn message-toggle [_]
  (-> message
      (assoc-in [1 :hx-get] (get rs/rs :home-message-toggle-reset))
      (assoc 2 "click me to reset the message")
      util/to-html))


(defn message-toggle-reset [_]
  (util/to-html message))

(def component
  [:<>
   [:br]
   message])