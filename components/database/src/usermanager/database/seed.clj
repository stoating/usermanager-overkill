(ns usermanager.database.seed
  (:require [clojure.edn :as edn]
            [next.jdbc.sql :as sql]
            [portal.api :as p]
            [xtdb.api :as xt]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def departments-seed
  (-> "./components/database/resources/database/departments.edn"
      slurp
      edn/read-string))


(def users-seed
  (-> "./components/database/resources/database/users.edn"
      slurp
      edn/read-string))


(defn seed
  [db]
  (when-not (seq (try
                   (xt/q db '(from :departments [*]))
                   (catch Exception _)))
    (try
      ;; populate departments table
      (doseq [[ix d] (map-indexed vector departments-seed)]
        (sql/insert! db :departments {:name d :xt$id (inc ix)}))

      ;; populate users table
      (doseq [a users-seed]
        (sql/insert! db :users (assoc a :xt$id (random-uuid))))

      (println "Populated database with initial data!")
      (catch Exception e
        (println "Exception:" (ex-message e))
        (println "Unable to populate the initial data -- proceed with caution!")))))