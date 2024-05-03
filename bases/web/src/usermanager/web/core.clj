(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [nextjournal.beholder :as beholder]
            [usermanager.time.interface :as time]
            [usermanager.log.interface :as logging]
            [usermanager.web.reload :as reload]
            [web.home :as home])
  (:gen-class))


(def config
  (-> "./web/config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defn eval-files! [cb]
  (let [eval-paths ["bases/web/resources"]
        on-eval nil]
    (println "eval-paths:" eval-paths)
    (println "on-eval:" on-eval)
    (println "callback content:" cb)
    (let [result (swap! reload/tracker-atom reload/refresh eval-paths)]
      (doseq [f on-eval]
        (f cb result))
      result)))


(def time-since-last-save (atom (java.util.Date.)))


(defn watcher-cb-actions [cb]
  (println "start watcher callback actions")
  (eval-files! cb)
  (println "finish watcher callback actions"))


(defn watcher-cb [cb]
  (println "watcher callback triggered.")
  (if (time/elapsed? @time-since-last-save :now 2 :seconds)
    ((Thread/sleep 100)
     (logging/catchall-verbose (watcher-cb-actions cb))
     (reset! time-since-last-save (java.util.Date.)))
    (println "spamming save, skip watcher callback actions")))


(def watcher
  (beholder/watch watcher-cb "bases/web/resources"))


(defmethod ig/init-key :app/filewatcher
  [key value]
  (println "starting:" key value)
  (println "- init filewatcher timer")
  time-since-last-save
  (println "- init filewatcher")
  watcher)


(defmethod ig/init-key :app/server
  [key value]
  (let [handler (get value :handler)
        options (-> value
                    (dissoc :handler)
                    (assoc :join? false))]
    (println "starting:" key value)
    (println "handler :" handler)
    (println "options :" options)
    (jetty/run-jetty handler options)))


(defmethod ig/init-key :app/home
  [key value]
  (let [name (get value :name)]
    (println "starting:" key value)
    (println "using   :" name)
    (fn [_] (resp/response (str home/layout)))))


(defmethod ig/halt-key! :app/filewatcher
  [key value]
  (println "stopping:" key value)
  (beholder/stop watcher))


(defmethod ig/halt-key! :app/server
  [key value]
  (let [server value]
    (println "stopping:" key value)
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
  )