(ns usermanager.filewatcher.interface
  (:require [usermanager.filewatcher.core :as core]
            [usermanager.filewatcher.actions :as actions]
            [usermanager.filewatcher.reload :as reload]))

(println "in ns:" (str *ns*))

(defn eval-files! [cb]
  (actions/eval-files! cb))

(def time-since-last-save
  core/time-since-last-save)

(def watcher
  core/watcher)

(def tracker-atom
  reload/tracker-atom)
