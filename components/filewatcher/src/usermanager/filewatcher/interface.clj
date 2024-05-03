(ns usermanager.filewatcher.interface
  (:require [usermanager.filewatcher.core :as core]))

(def time-since-last-save
  core/time-since-last-save)

(def watcher
  core/watcher)