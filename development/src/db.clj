(ns db
  (:require [clojure.pprint :as pp]
            [usermanager.database.seed :as seed]
            [usermanager.database.interface :as db]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]))

(comment
  ;; create connection to db
  (def db (xtc/start-client "http://localhost:6543"))
  (xt/status db)

  ;; populate users
  ;; the print ensures the dom updates correctly?
  (def populate-users
    (do (doseq [cur-user seed/users-seed]
          (db/insert-user db cur-user))
        (pp/pprint (xt/q db '(from :users [*])))))

  ;; debug
  (pp/pprint (xt/q db '(from :departments [*])))
  (pp/pprint (xt/q db '(from :users [*])))
)