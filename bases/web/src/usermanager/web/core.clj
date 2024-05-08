(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [nextjournal.beholder :as bh]
            [ring.adapter.jetty :as jetty]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.routes :as routes])
  (:gen-class))


(println "in ns:" (str *ns*))


(def config
  (-> "./config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defmethod ig/init-key :app/filewatcher
  [key _]
  (println "starting:" key)
  fw/time-since-last-save
  (fw/watcher))


(defmethod ig/init-key :app/server
  [key value]
  (let [handler (get value :handler)
        options (-> value
                    (dissoc :handler)
                    (assoc :join? false))]
    (println "starting:" key)
    (jetty/run-jetty handler options)))


(defmethod ig/init-key :app/app
  [key value]
  (println "starting:" key)
  (println "using   :" value)
  (routes/app))


#_(defmethod ig/init-key :app/database
  [key value]
  (let [handler (get value :handler)
        options (-> value
                    (dissoc :handler)
                    (assoc :join? false))]
    (println "starting:" key)))


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
  (try
    (ig/init config)
    (catch Exception e
      (println (str "caught exception: " (.getMessage e)))
      (println "the server is likely already started"))))


(defn -main []
  system)


(comment
  (ig/halt! system)

  (ig/suspend! system)

  (ig/resume config system)
  )
