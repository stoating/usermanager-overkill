(ns usermanager.log.interface
  (:require [usermanager.log.core :as core]))

(println "in ns:" (str *ns*))

(defn catchall-verbose [& body]
  (core/catchall-verbose body))