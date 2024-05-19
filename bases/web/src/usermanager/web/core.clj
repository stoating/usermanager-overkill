(ns usermanager.web.core
  (:require [aero.core :as aero]
            [clojure.java.io :as io]
            [clojure.pprint :as pp]
            [integrant.core :as ig]
            [next.jdbc.sql :as sql]
            [next.jdbc.xt]
            [portal.api :as p]
            [ring.adapter.jetty :as jetty]
            [usermanager.web.routes :as routes]
            [xtdb.api :as xt]
            [xtdb.client :as xtc])
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
  (let [xtdb-url (str url ":" port)]
    (println "starting:" key)
    (println "url:" xtdb-url)
    (with-open [node (xtc/start-client xtdb-url)]
      (xt/status node))))


(defmethod ig/halt-key! :app/server
  [key server]
  (println "stopping:" key)
  (println "using   :" server)
  (.stop server))


(defn -main []
  (ig/init config))


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