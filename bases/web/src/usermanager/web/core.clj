(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [nextjournal.beholder :as bh]
            [web.home :as home]
            [web.filewatcher :as fw])
  (:gen-class))


(def config
  (-> "./web/config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defmethod ig/init-key :app/filewatcher
  [key _]
  (println "starting:" key)
  #_(println "- init filewatcher timer")
  fw/time-since-last-save
  #_(println "- init filewatcher")
  fw/watcher)


(defmethod ig/init-key :app/server
  [key value]
  (let [handler (get value :handler)
        options (-> value
                    (dissoc :handler)
                    (assoc :join? false))]
    (println "starting:" key)
    #_(println "options :" options)
    (jetty/run-jetty handler options)))


(defmethod ig/init-key :app/home
  [key value]
  (let [name (get value :name)]
    (println "starting:" key)
    #_(println "using   :" name)
    (fn [_] (resp/response (str home/layout)))))


(defmethod ig/halt-key! :app/filewatcher
  [key _]
  (println "stopping:" key)
  (bh/stop fw/watcher))


(defmethod ig/halt-key! :app/server
  [key value]
  (let [server value]
    (println "stopping:" key)
    (.stop server)))


(def system
  (ig/init config))


(defn -main []
  system)


(comment
  ;; shutdown system
  (ig/halt! system)

  ;; suspend system
  (ig/suspend! system)

  (ig/resume config system)
  )