(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))

(defn get-departments [db]
  (xt/q db '(from :departments [*])))

(defn get-department-by-id [db id]
  (-> (xt/q db '(-> (from :departments [name xt/id])
                    (return name)))
      (nth id)))

(defn get-users [db]
  (xt/q db '(from :users [*])))

(defn get-user-by-id [db id]
  (-> (xt/q db '(-> (from :user [name xt/id])
                    (return name))))
  (-> (filter #(= (:xt/id %) id) db)
      first))

