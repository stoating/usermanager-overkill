(ns usermanager.log.interface
  #_(:require #_[usermanager.log.core :as core]))

(println "in ns:" (str *ns*))

#_(defn catchall-verbose [& body]
  (core/catchall-verbose body))

(println "end ns:" (str *ns*))