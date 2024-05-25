(ns usermanager.database.transactions
  (:require [portal.api :as p]
            [usermanager.database.queries :as q]
            [xtdb.api :as xt]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn insert-department
  ([db name]
   (let [max-id (q/get-departments-max-id db)]
     (if (nil? max-id)
       (insert-department db name 0)
       (insert-department db name (inc max-id)))))
  ([db name id]
   (xt/submit-tx db [[:put-docs :departments
                      {:name name
                       :xt/id id}]])))


(defn insert-user
  [db user]
  (xt/submit-tx db [[:put-docs :users
                     (assoc user :xt/id (random-uuid))]]))