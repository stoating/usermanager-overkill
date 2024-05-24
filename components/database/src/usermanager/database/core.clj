(ns usermanager.database.core
  (:require [xtdb.api :as xt]
            [xtdb.client :as xtc]
            [usermanager.database.seed :as seed]))

(defn init-db [url]
  (with-open [db (xtc/start-client url)]
    (xt/status db)
    (seed/seed db)
    db))