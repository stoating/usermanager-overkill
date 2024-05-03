(ns usermanager.web.util
  (:require [clojure.stacktrace :as st]))

(defmacro catchall-verbose
  [& body]
  `(try
     ~@body
     (catch Exception e#
       (st/print-stack-trace e#))))