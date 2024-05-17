(ns usermanager.web.core
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [next.jdbc.sql :as sql]
            [next.jdbc.xt]
            [nextjournal.beholder :as bh]
            [portal.api :as p]
            [ring.adapter.jetty :as jetty]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.routes :as routes]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]
            [clojure.pprint :as pp])
  (:gen-class))


(println "in ns:" (str *ns*))


(def config
  #_(-> "./config.edn"
      (io/resource)
      (slurp)
      (ig/read-string))
  {:app/filewatcher {}
   :app/server {:port 8080
                :handler (ig/ref :app/app)}
   :app/app {:db (ig/ref :app/database)}
   :app/database {}})


(defmethod ig/init-key :app/filewatcher
  [key _]
  (println "starting:" key)
  fw/time-since-last-save
  fw/watcher)


(defmethod ig/init-key :app/server
  [key {:keys [handler] :as opts}]
  (println "starting:" key)
  (println "opts   :" opts)
  (println "handler:" handler)
  (try
    (jetty/run-jetty handler (-> opts
                                 (dissoc :handler)
                                 (assoc :join? false)))
    (catch Exception e
      (println "Error" (ex-message e)))))


(defmethod ig/init-key :app/app
  [key value]
  (println "starting:" key)
  (println "using   :" value)
  (routes/app))


(defmethod ig/init-key :app/database
  [key {:keys [url port]}]
  (println "starting:" key)
  (println "url:" url)
  (println "port:" port)
  (with-open [node (xtc/start-client "http://localhost:6543")]
    (xt/status node)))


(defmethod ig/halt-key! :app/server
  [key server]
  (println "stopping:" key)
  (println "using   :" server)
  (.stop server))


(def system
  (ig/init config))


(defn -main []
  system)


(comment
  (ig/halt! system)
  (ig/init config)
  (ig/suspend! system)
  (ig/resume config system)
  (-main)
  )


(comment
  (println "hi")
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)

  (def myperson [{:name "honk"
                  :xt/id (random-uuid)}])

  (sql/insert! mynode :department (assoc {:name "honk"}
                                         :xt$id (random-uuid)))
  (sql/insert! mynode :addressbook (assoc {:first_name "kimma"
                                           :last_name "name"
                                           :email "my-email@gmail.com"
                                           :department_id "4"}
                                          :xt$id (random-uuid)))
  (xt/submit-tx mynode (:department {:name "honk"
                                     :xt$id (random-uuid)}))
  (def newnode (assoc mynode :database-type "xtdb"))
  (sql/query mynode ["select * from department"])
  (sql/query mynode ["select * from information_schema.tables"])
  (xt/q mynode '(-> (from :department [*])
                    (order-by {:val name
                               :dir :asc
                               :nulls :last})
                    (limit 2)))
  (pp/pprint (xt/q mynode '(from :xt$txs [*])))
  (pp/pprint (xt/q mynode '(from :addressbook [*])))
  (def myquery (xt/q mynode '(from :addressbook [*])))
  (tap> myquery)
  (xt/q mynode '(from :department
                      [{:name name}]
                      {name "immaname"}))
  (xt/q mynode '(from :department [name]))
  (xt/q mynode '(from :department [xt/id]))
  (xt/q mynode '(from :department [name xt/id]))

  (pp/pprint
   (xt/q mynode '(from :information_schema.tables
                       [table_name
                        table_type
                        table_catalog
                        table_schema])))

  (tap> mynode)
  (tap> newnode)
  (def portal (p/open))
  (add-tap #'p/submit))

(println "end ns:" (str *ns*))