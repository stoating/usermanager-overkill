(ns usermanager.database.interface
  (:require [usermanager.database.core :as core]
            #_[usermanager.database.queries :as query]))

(defn init-db [url]
  (core/init-db url))

#_(defn get-users [db]
  (query/get-users db))