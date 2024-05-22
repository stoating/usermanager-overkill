(ns usermanager.web.database.seed
  (:require [clojure.edn :as edn]
            [next.jdbc.sql :as sql]
            [portal.api :as p]
            [xtdb.api :as xt]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def departments-seed
  (-> "./bases/web/resources/database/seed/departments.edn"
      slurp
      edn/read-string))


(def users-seed
  (-> "./bases/web/resources/database/seed/users.edn"
      slurp
      edn/read-string))


(defn seed
  [db]
  (when-not (seq (try
                   (xt/q db '(from :departments [*]))
                   (catch Exception _)))
    (tap> db)
    (try
      (doseq [[ix d] (map-indexed vector departments-seed)]
        (sql/insert! db :departments {:name d :xt$id (inc ix)}))
      (doseq [a users-seed]
        (sql/insert! db :users (assoc a :xt$id (random-uuid))))
      (println "Populated database with initial data!")
      (catch Exception e
        (println "Exception:" (ex-message e))
        (println "Unable to populate the initial data -- proceed with caution!")))
    (tap> (xt/q db '(from :users [*])))
    (tap> (xt/q db '(from :departments [*])))))