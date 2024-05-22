(ns user
  (:require [integrant.repl :as ig-repl]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.core :as web]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]))

(println "in ns:" (str *ns*))

(integrant.repl/set-prep! (fn [] web/config))

(comment
  (defonce start-app
    (do (fw/watcher)
        (ig-repl/go)))

  (ig-repl/go)
  (ig-repl/halt)
  (ig-repl/reset)

  (ig-repl/prep)
  (ig-repl/init)
  (ig-repl/clear)
  (ig-repl/reset-all)


  )

(comment
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)

  (xt/q mynode '(from :departments [*]))
  (xt/q mynode '(from :users [*]))
  (get (first (xt/q mynode '(-> (from :users [first-name])
                                (order-by first-name))))
       :first-name)

  (get-in (first [{:first-name "John"}]) [:first-name])
  (get (first [{:first-name "John"}]) :first-name)
  )