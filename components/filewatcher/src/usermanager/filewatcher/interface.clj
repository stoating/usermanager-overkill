(ns usermanager.filewatcher.interface
  (:require [usermanager.filewatcher.core :as core]))

(println "in ns:" (str *ns*))

(defn watcher []
  (core/watcher))