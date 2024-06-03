(ns user
  (:require [integrant.repl :as ig-repl]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.core :as web]))

(println "in ns:" (str *ns*))

(integrant.repl/set-prep! (fn [] web/config))

(comment
  ;; at startup. run once.
  (defonce start-app
    (do (fw/watcher)
        (ig-repl/go)))

  ;; run to restart system
  (ig-repl/reset)

  ;; run to start flow-storm debugger
  :dbg

  (ig-repl/go)
  (ig-repl/halt)
  (ig-repl/prep)
  (ig-repl/init)
  (ig-repl/clear)
  (ig-repl/reset-all)
  )