(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [ring.util.response :as resp]
            [hiccup2.core :as h]
            [nextjournal.beholder :as beholder])
  (:gen-class))


(def layout
  (h/html [:head
           [:title "Hello"]]
          [:body
           [:h1 "Hello, world"]
           [:div {:id "app"}]]))


(def config
  (-> "./web/config.edn"
      (io/resource)
      (slurp)
      (ig/read-string)))


(defn watcher-cb [cb]
  (println "File changed")
  (prn cb))


(defn watcher []
  (beholder/watch watcher-cb "bases/web/src"))


(defmethod ig/init-key :app/filewatcher
  [_ _]
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