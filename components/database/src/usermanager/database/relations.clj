(ns usermanager.database.relations
  (:require [usermanager.database.queries :as q]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]))


(defn user->department
  [db user]
  (->> (get user :department-id)
       (q/get-department-by-id db)))


(comment
  (require '[clojure.pprint :as pp])
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)

  (pp/pprint (xt/q mynode '(from :departments [*])))
  (pp/pprint (xt/q mynode '(from :users [*])))

  (def myuser (first (q/get-users mynode)))
  myuser
  (q/get-department-by-id mynode (get myuser :department-id))
  (user->department mynode myuser)
  )