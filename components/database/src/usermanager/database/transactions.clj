(ns usermanager.database.transactions
  (:require [malli.core :as m]
            [malli.dev.pretty :as pretty]
            [portal.api :as p]
            [usermanager.database.queries :as q]
            [usermanager.database.schema :as schema]
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
     (if (m/validate schema/Department name)
       (if (nil? max-id)
         (insert-department-p db name 0)
         (insert-department-p db name (inc max-id)))
       (pretty/explain schema/Department name)))))


(defn delete-user-by-id
  [db id]
  (xt/submit-tx db [[:delete-docs :users :xt/id id]]))


(defn department-id-exists? [db id]
  (try
    (q/get-department-by-id db id)
    (catch Exception e (.getMessage e)
           false)))


(defn- insert-user-p
  [db user]
  (xt/submit-tx db [[:put-docs :users
                     (assoc user :xt/id (random-uuid))]]))


(defn insert-user
  [db user]
  (if (m/validate schema/User user)
    (let [id (:department-id user)]
      (if (department-id-exists? db id)
        (insert-user-p db user)
        (println (format "Warning: id %d does not exist in Departments table" id))))
    (pretty/explain schema/User user)))


(defn update-user-p
  [db user]
  (xt/submit-tx db
                [[:update '{:table :users
                            :bind [{:xt/id $xt/id}]
                            :set {:first-name $first-name
                                  :last-name $last-name
                                  :email $email
                                  :department-id $department-id}}
                  user]]))


(defn update-user
  [db user]
  (if (m/validate schema/User user)
    (let [id (user :department-id)]
      (if (department-id-exists? db id)
        (update-user-p db user)
        (println (format "Warning: department id %d does not exist" id))))
    (pretty/explain schema/User user)))


(comment
  (require '[clojure.pprint :as pp])
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)
  (pp/pprint (xt/q mynode '(from :users [*])))
  (xt/q mynode '(from :departments [*]))
  )