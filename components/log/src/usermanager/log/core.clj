(ns usermanager.log.core
  (:require [clojure.stacktrace :as st]))

(defmacro catchall-verbose
  [& body]
  `(try
     ~@body
     (catch Exception e#
       (st/print-stack-trace e#))))