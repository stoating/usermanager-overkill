(ns usermanager.database.seed
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [portal.api :as p]
            [usermanager.database.transactions :as tx]
            [xtdb.api :as xt]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def departments-seed
  (-> "database/departments.edn"
      io/resource
      slurp
      edn/read-string))


(def users-seed
  (-> "database/users.edn"
      io/resource
      slurp
      edn/read-string))


(defn seed
  [db]
  (when-not (seq (try
                   (xt/q db '(from :departments [*]))
                   (catch Exception _)))
    (try
      (println "Seeding database with initial data!")
      ;; populate departments table
      (doseq [cur-department (map vector departments-seed)]
        (tx/insert-department db (first cur-department)))

      ;; populate users table
      (doseq [cur-user users-seed]
        (tx/insert-user db cur-user))

      (println "Seeded database with initial data!")
      (catch Exception e
        (println "Exception:" (ex-message e))
        (println "Unable to populate the initial data -- proceed with caution!")))))


(comment
  (map-indexed vector departments-seed)
  departments-seed
  (map vector departments-seed)
  (doseq [hi '(["Accounting"] ["Sales"] ["Support"] ["Development"])]
    (println "hi:" (first hi)))
  (first (first '(["Accounting"] ["Sales"] ["Support"] ["Development"])))
  )