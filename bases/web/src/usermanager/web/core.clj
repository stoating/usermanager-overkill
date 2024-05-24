(ns usermanager.web.core
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [next.jdbc.xt]
            [portal.api :as p]
            [ring.adapter.jetty :as jetty]
            [usermanager.database.interface :as db]
            [usermanager.web.routes :as routes])
  (:gen-class))


(println "in ns:" (str *ns*))


(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))


(def config
  (-> "./config.edn"
      (io/resource)
      (aero/read-config)))


(defmethod ig/init-key :app/server
  [key {:keys [handler] :as opts}]
  (println "starting:" key)
  (println "opts    :" opts)
  (println "handler :" handler)
  (try
    (jetty/run-jetty handler (-> opts
                                 (dissoc :handler)
                                 (assoc :join? false)))
    (catch Exception e
      (println "Error" (ex-message e)))))


(defmethod ig/init-key :app/app
  [key {:keys [db]}]
  (println "starting:" key)
  (println "using   :" db)
  (routes/app db))


(defmethod ig/init-key :app/database
  [key {:keys [url port]}]
  (let [xtdb-url (str url ":" port)]
    (println "starting:" key)
    (println "url     :" xtdb-url)
    (db/init-db xtdb-url)))


(defmethod ig/init-key :app/portal
  [key value]
  (println "starting:" key)
  (println "using   :" value)
  (defonce portal (p/open))
  (println "portal  :" portal))


(defmethod ig/halt-key! :app/server
  [key server]
  (println "stopping:" key)
  (println "using   :" server)
  (.stop server))


(defn -main []
  (ig/init config))