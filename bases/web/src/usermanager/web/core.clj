(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp])
  (:gen-class))


;; initialized recursively
;; app/greet -> app/server
(def config
  (-> "./web/config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defmethod ig/init-key :app/server
  [_ {:keys [handler] :as opts}]
  (println "Starting" opts)
  (jetty/run-jetty handler (-> opts
                               (dissoc :handler)
                               (assoc :join? false))))


(defmethod ig/halt-key! :app/server
  [_ server]
  (println "Stopping" server)
  (.stop server))


(defmethod ig/init-key :handler/greet
  [_ {:keys [name]}]
  (println "Starting" name)
  (fn [_] (resp/response (-> "./web/mypage.html"
                             (io/resource)
                             slurp))))


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