(ns usermanager.web.database.seed
  (:require [next.jdbc.sql :as sql]
            [portal.api :as p]
            [xtdb.api :as xt]))

(def departments-seed
  ["Accounting"
   "Sales"
   "Support"
   "Development"])

(def user-data-seed
  [{:first_name "Sean"
    :last_name "Corfield"
    :email "sean@worldsingles.com"
    :department_id 4}])

(defn seed
  [db]
  (when-not (seq (try
                   (xt/q db '(from :department [*]))
                   (catch Exception _)))
    (tap> db)
    (try
      (doseq [[ix d] (map-indexed vector departments-seed)]
        (sql/insert! db :department {:name d :xt$id (inc ix)}))
      (doseq [a user-data-seed]
        (sql/insert! db :addressbook (assoc a :xt$id (random-uuid))))
      (println "Populated database with initial data!")
      (catch Exception e
        (println "Exception:" (ex-message e))
        (println "Unable to populate the initial data -- proceed with caution!")))
    (tap> (xt/q db '(from :addressbook [*])))
    (tap> (xt/q db '(from :department [*])))))

(defonce portal (p/open))
(add-tap #'p/submit)