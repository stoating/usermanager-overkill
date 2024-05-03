(ns usermanager.filewatcher.core
  (:require [nextjournal.beholder :as beholder]
            [usermanager.time.interface :as time]
            [usermanager.log.interface :as logging]
            [usermanager.filewatcher.actions :as actions]))


(def time-since-last-save (atom (java.util.Date.)))


(defn watcher-cb-actions [cb]
  (println "start watcher callback actions")
  (actions/eval-files! cb)
  (println "finish watcher callback actions"))


(defn watcher-cb [cb]
  (println "watcher callback triggered.")
  (if (time/elapsed? @time-since-last-save :now 2 :seconds)
    ((Thread/sleep 200)
     (logging/catchall-verbose (watcher-cb-actions cb))
     (reset! time-since-last-save (java.util.Date.)))
    (println "spamming the filewatcher, skip watcher callback actions")))


(def watcher
  (beholder/watch watcher-cb "bases/web/resources" "components"))