(ns usermanager.time.interface
  (:require [usermanager.time.core :as core]))

(println "in ns:" (str *ns*))

(defn elapsed? [t1 t2 x unit]
  (core/elapsed? t1 t2 x unit))
