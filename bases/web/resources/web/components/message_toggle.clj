(ns web.components.message-toggle
  (:require [routes :as rs]
            [usermanager.web.controller.util :as util]))


(def component
  [:button {:hx-get (get rs/rs :default-message-toggle)
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "click me to toggle message"])


(defn message-toggle [_]
  (-> component
      (assoc-in [1 :hx-get] (get rs/rs :default-message-toggle-reset))
      (assoc 2 "click me to reset the message")
      util/to-html))


(defn message-toggle-reset [_]
  (util/to-html component))