(ns web.components.message-toggle
  (:require [routes :as rs]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))


(def component
  [:button {:hx-get (get rs/rs :default-message-toggle)
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "click me to toggle message"])


(defn message-toggle [_]
  (-> component
      (assoc-in [1 :hx-get] (get rs/rs :default-message-toggle-reset))
      (assoc 2 "click me to reset the message")
      util/hiccup->html-resp))


(defn message-toggle-reset [_]
  (util/hiccup->html-resp component))