(ns dev
  (:require [integrant.repl
             :refer [clear go halt prep init reset reset-all]
             :as ig-repl]
            [usermanager.web.core :as system]))

(ig-repl/set-prep! (fn [] system/config))

(comment
  (clear)
  (prep)
  (init)
  (go)
  (halt)
  (reset)
  (reset-all)
  )