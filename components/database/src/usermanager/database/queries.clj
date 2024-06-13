(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))


(defn get-departments [db]
  (xt/q db '(-> (from :departments [*])
                (order-by {:val name :dir :asc}))))


(defn get-department-by-id [db id]
  (-> (xt/q db '(from :departments [* {:xt/id $id}])
            {:args {:id id}})
      (first)))


(defn get-departments-max-id [db]
  (-> (xt/q db '(-> (from :departments [xt/id])
                    (order-by {:val xt/id :dir :desc})
                    (limit 1)))
      (first)
      (:xt/id)))


(comment
  (require '[clojure.pprint :as pp]
           '[xtdb.client :as xtc])
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)
  (pp/pprint (xt/q mynode '(from :users [*])))
  (xt/q mynode '(from :departments [*]))
  (get-departments-max-id mynode)
  (get-in [{:name "Accounting" :xt/id 1}] [0 :xt/id])
  (-> [{:name "Accounting" :xt/id 1}]
      (first)
      (:xt/id))
  )


(defn get-users [db]
  (xt/q db '(-> (from :users [*])
                (order-by {:val last-name :dir :asc}))))


(defn get-user-by-id [db id]
  (-> (xt/q db '(from :users [* {:xt/id $id}])
            {:args {:id id}})
      (first)))