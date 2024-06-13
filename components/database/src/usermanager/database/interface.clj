(ns usermanager.database.interface
  (:require [usermanager.database.core :as core]
            [usermanager.database.queries :as q]
            [usermanager.database.transactions :as tx]
            [usermanager.database.relations :as rel]))

;; init db
(defn init-db [url]
  (core/init-db url))


;; queries
(defn get-users
  [db]
  (q/get-users db))

(defn get-user-by-id
  [db id]
  (q/get-user-by-id db id))

(defn get-departments
  [db]
  (q/get-departments db))


;; transactions
(defn delete-user-by-id
  [db id]
  (tx/delete-user-by-id db id))

(defn insert-user
  [db user]
  (tx/insert-user db user))

(defn update-user
  [db user]
  (tx/update-user db user))


;; relations
(defn user->department
  [db user]
  (rel/user->department db user))
