(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [nextjournal.beholder :as bh]
            [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [usermanager.filewatcher.interface :as fw]
            [web.home :as home])
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


(defn default [req]
  (assoc-in req [:params :message]
            (str "Welcome to the User Manager application demo! "
                 "This uses just Reitit, Ring, and Selmer.")))

(defn render-page
  []
  (-> (resp/response (str home/layout))
      (resp/content-type "text/html")))


(defn my-middleware
  [handler]
  (fn [req]
    (let [resp (handler req)]
      (if (resp/response? resp)
        resp
        (render-page)))))


(defn app []
  (ring/ring-handler
   (ring/router
    [["/" {:handler default}]]
    {:data {:middleware [my-middleware
                         parameters/parameters-middleware
                         wrap-keyword-params]}})
   (ring/routes
    (ring/create-resource-handler
     {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})}))))


(defmethod ig/init-key :app/run-app
  [key value]
  (println "starting:" key)
  (println "using   :" value)
  (app))

#_(defmethod ig/init-key :app/home
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


(defonce system
  (ig/init config))


(defn -main []
  system)


(comment
  (ig/halt! system)

  (ig/suspend! system)

  (ig/resume config system))
