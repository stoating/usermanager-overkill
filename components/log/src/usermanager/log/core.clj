(ns usermanager.log.core
  #_(:require #_[clojure.stacktrace :as st]))

(println "in ns:" (str *ns*))

#_(defmacro catchall-verbose
  [& body]
  `(try
     ~@body
     (catch Exception e#
       (st/print-stack-trace e#))))

(println "end ns:" (str *ns*))