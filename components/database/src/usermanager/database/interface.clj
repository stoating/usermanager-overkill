(ns usermanager.database.interface
  (:require [usermanager.database.core :as core]
            [usermanager.database.queries :as query]))

(defn init-db [url]
  (core/init-db url))

(defn get-users [db]
  (query/get-users db))