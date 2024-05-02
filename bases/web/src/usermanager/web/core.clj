(ns usermanager.web.core
  (:require [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp])
  (:gen-class))


;; initialized recursively
;; handler/greet -> adapter/jetty
(def config
  {:adapter/jetty {:port 8080
                   :handler (ig/ref :handler/greet)}
   :handler/greet {:name "Alice"}})


(defmethod ig/init-key :adapter/jetty
  [_ {:keys [handler] :as opts}]
  (jetty/run-jetty handler (-> opts (dissoc :handler) (assoc :join? false))))


(defmethod ig/init-key :handler/greet
  [_ {:keys [name]}]
  (fn [_] (resp/response (str "Hello " name))))


(defmethod ig/halt-key! :adapter/jetty [_ server]
  (.stop server))


(def system
  (ig/init config))


(defn -main []
  system)

(comment
  ;; shutdown system
  (ig/halt! system)

  ;; suspend system
  (ig/suspend! system)
  )