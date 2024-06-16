(ns usermanager.web.core
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [integrant.core :as ig]
            [ring.adapter.jetty :as jetty]
            [usermanager.database.interface :as db]
            [usermanager.web.routes :as routes])
  (:gen-class))


(println "in ns:" (str *ns*))


(defmethod aero/reader 'ig/ref
  [_ _ value]
  (ig/ref value))


(defn config [env]
  (-> "config.edn"
      (io/resource)
      (aero/read-config env)))


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
  [key {:keys [state db]}]
  (println "starting:" key)
  (println "using state:" state)
  (println "using db   :" db)
  (routes/app (atom state) db))


(defmethod ig/init-key :app/database
  [key {:keys [host-name port]}]
  (let [xtdb-url (str "http://" host-name ":" port)]
    (println "starting:" key)
    (println "url     :" xtdb-url)
    (db/init-db xtdb-url)))


(defmethod ig/halt-key! :app/server
  [key server]
  (println "stopping:" key)
  (println "using   :" server)
  (.stop server))


(defn -main []
  (ig/init (config {:profile :prod})))