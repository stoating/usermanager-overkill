(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [hiccup2.core :as h]
            [nextjournal.beholder :as beholder]
            [usermanager.web.time :as time]
            [usermanager.web.util :as util]
            [usermanager.web.reload :as reload])
  (:gen-class))


(def layout
  (h/html [:head
           [:title "Hello"]]
          [:body
           [:h1 "Hello, world aaaa"]
           [:div {:id "app"}]]))


(def config
  (-> "./web/config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defn eval-files!
  [{:keys [eval-paths on-eval]
    :or {eval-paths ["bases/web/src"]}
    :as ctx}]
  (println "eval paths" eval-paths)
  (println "on eval" on-eval)
  (println "context" ctx)
  (let [result (swap! reload/global-tracker reload/refresh eval-paths)]
    (doseq [f on-eval]
      (f ctx result))
    result))


(def last-called (atom (java.util.Date.)))


(defn watcher-cb-actions [cb]
  (println "start watcher callback actions")
  (eval-files! cb)
  (println "finish watcher callback actions"))


(defn watcher-cb [cb]
  (println "watcher callback triggered.")
  (if (time/elapsed? @last-called :now 2 :seconds)
    ((println "perform watcher callback actions")
     (Thread/sleep 100)
     (util/catchall-verbose (watcher-cb-actions cb))
     (reset! last-called (java.util.Date.)))
    (println "saved too soon, skip watcher callback actions")))


(defn watcher []
  (beholder/watch watcher-cb "bases/web/src"))


(defmethod ig/init-key :app/filewatcher
  [_ _]
  (println "Setting filewatcher timer")
  last-called
  (println "Starting filewatcher")
  (watcher))


(defmethod ig/halt-key! :app/filewatcher
  [_ _]
  (println "Stopping filewatcher")
  (beholder/stop watcher))


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
  (fn [_] (resp/response (str (h/html layout)))))


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