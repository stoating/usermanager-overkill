(ns usermanager.database.transactions
  (:require [portal.api :as p]
            [usermanager.database.queries :as q]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn- insert-department-p
  [db name id]
  (xt/submit-tx db [[:put-docs :departments
                     {:name name
                      :xt/id id}]]))

(defn insert-department
  ([db name]
   (let [max-id (q/get-departments-max-id db)]
     (if (nil? max-id)
       (insert-department-p db name 0)
       (insert-department-p db name (inc max-id))))))


(defn delete-user-by-id
  [db id]
  (xt/submit-tx db [[:delete-docs :users id]]))


(defn insert-user
  [db user]
  (xt/submit-tx db [[:put-docs :users
                     (assoc user :xt/id (random-uuid))]]))


(comment
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)
  (insert-department mynode "Engineering")
  (insert-user mynode {:first-name "Bonk"
                       :last-name "Bonk"
                       :email "honk@gmail.com"})
  (xt/q mynode '(from :users [*]))
  (xt/q mynode '(from :departments [*]))
  (xt/submit-tx mynode [[:delete-docs :users
                         #uuid "e6d0122d-08ba-4cfd-99bf-c9b027483a6f"]])
  (q/get-user-by-id mynode #uuid "e6d0122d-08ba-4cfd-99bf-c9b027483a6f")
  (delete-user-by-id mynode #uuid "e6d0122d-08ba-4cfd-99bf-c9b027483a6f")
  )