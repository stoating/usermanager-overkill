(ns user
  (:require [integrant.repl :as ig-repl]
            [portal.api :as p]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.core :as web]))

(println "in ns:" (str *ns*))

(integrant.repl/set-prep! (fn [] (web/config {:profile :dev})))

(comment
  ;; run once after connecting to repl
  ;; starts filewatcher and web server
  (defonce start-app
    (do (fw/watcher)
        (ig-repl/go)))

  ;; run to restart system
  (ig-repl/reset)

  ;; run to start portal debugger
  (p/open)

  ;; run to start flow-storm debugger
  :dbg

  (ig-repl/go)
  (ig-repl/halt)
  (ig-repl/prep)
  (ig-repl/init)
  (ig-repl/clear)
  (ig-repl/reset-all)
  )