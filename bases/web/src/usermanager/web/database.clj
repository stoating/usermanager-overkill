(ns usermanager.web.database
  (:require [next.jdbc.sql :as sql]
            [next.jdbc.xt] ; activate XTDB support
            [portal.api :as p]))

(println "in ns:" (str *ns*))

(comment

  (def ^:private departments
    "List of departments."
    ["Accounting" "Sales" "Support" "Development"])


  (def ^:private initial-user-data
    "Seed the database with this data."
    [{:first_name "Sean" :last_name "Corfield"
      :email "sean@worldsingles.com" :department_id 4}])


;; database initialization

  (defn populate
    "Called at application startup. If the departments are
   missing, insert the initial data."
    [db]
    (when-not (seq (try
                     (sql/query (db) ["select * from department"])
                     (catch Exception _)))
      (tap> db)
      (try
        (doseq [[ix d] (map-indexed vector departments)]
          (sql/insert! (db) :department {:name d :xt$id (inc ix)}))
        (doseq [a initial-user-data]
          (sql/insert! (db) :addressbook (assoc a :xt$id (random-uuid))))
        (println "Populated database with initial data!")
        (catch Exception e
          (println "Exception:" (ex-message e))
          (println "Unable to populate the initial data -- proceed with caution!")))))

)
(comment
  (def portal (p/open))

  (add-tap #'p/submit)
  )

(println "end ns:" (str *ns*))