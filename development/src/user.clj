(ns user
  (:require [integrant.repl :as ig-repl]
            [portal.api :as p]
            [usermanager.web.core :as web]))

(println "in ns:" (str *ns*))

(integrant.repl/set-prep! (fn [] web/config))

(comment
  (ig-repl/go)
  (ig-repl/halt)
  (ig-repl/reset)


  (ig-repl/prep)
  (ig-repl/init)
  (ig-repl/clear)
  (ig-repl/reset-all)

  (def portal (p/open))
  (add-tap #'p/submit)
  )

(println "end ns:" (str *ns*))