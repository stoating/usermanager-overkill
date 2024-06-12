(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))


(defn get-department-by-id [db id]
  (-> (xt/q db '(-> (from :departments [name xt/id])
                    (order-by {:val xt/id :dir :asc})
                    (return name)))
      (nth id)))


(defn get-departments [db]
  (xt/q db '(-> (from :departments [*])
                (order-by {:val name :dir :asc}))))


(defn get-departments-max-id [db]
  (-> (xt/q db '(-> (from :departments [xt/id])
                    (order-by {:val xt/id :dir :desc})
                    (limit 1)))
      (get-in [0 :xt/id])))


#_(defn get-user-by-id [db id]
  (->> (xt/q db '(from :users [*]))
       (filter (fn [user] (= (:xt/id user) id)))
       first))


(defn get-users [db]
  (xt/q db '(-> (from :users [*])
                (order-by {:val last-name :dir :asc}))))