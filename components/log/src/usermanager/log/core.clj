(ns usermanager.log.core
  (:require [clojure.stacktrace :as st]))

(println "in ns:" (str *ns*))

(defmacro catchall-verbose
  [& body]
  `(try
     ~@body
     (catch Exception e#
       (st/print-stack-trace e#))))