(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))

(defn get-users [db]
  (xt/q db '(from :users [*])))

(comment
  )