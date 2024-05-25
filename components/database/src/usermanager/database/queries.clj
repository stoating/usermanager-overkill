(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))


#_(defn get-department-by-id [db id]
  (-> (xt/q db '(-> (from :departments [name xt/id])
                    (return name)))
      (nth id)))


#_(defn get-departments [db]
  (xt/q db '(from :departments [*])))


(defn get-departments-max-id [db]
  (-> (xt/q db '(-> (from :departments [xt/id])
                    (order-by {:val xt/id :dir :desc})
                    (limit 1)))
      (get-in [0 :xt/id])))


(defn get-user-by-id [db id]
  (->> (xt/q db '(from :users [*]))
       (filter #(= (:xt/id %) id))
       first))


(defn get-users [db]
  (xt/q db '(from :users [*])))
