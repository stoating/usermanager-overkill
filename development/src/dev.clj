(ns dev
  (:require [integrant.core :as ig]
            [portal.api :as p]
            [usermanager.web.core :as web]))

(println "in ns:" (str *ns*))

(comment
  ;; restart system
  (ig/halt! web/system)
  (ig/init web/config)

  (def portal (p/open))
  (add-tap #'p/submit))

(println "end ns:" (str *ns*))