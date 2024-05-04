(ns usermanager.filewatcher.core
  (:require [nextjournal.beholder :as beholder]
            [usermanager.filewatcher.actions :as actions]
            [usermanager.log.interface :as logging]
            [usermanager.time.interface :as time]))

(println "in ns:" (str *ns*))

(def time-since-last-save (atom (java.util.Date.)))


(defn watcher-cb-actions [cb]
  (println "start watcher callback actions")
  (actions/eval-files! cb)
  (println "finish watcher callback actions"))


(defn watcher-cb [cb]
  (println "watcher callback triggered.")
  (if (time/elapsed? @time-since-last-save :now 3 :seconds)
    ((Thread/sleep 300)
     (logging/catchall-verbose (watcher-cb-actions cb))
     (reset! time-since-last-save (java.util.Date.)))
    (println "spamming filewatcher, skip callback actions")))


(def watcher
  (beholder/watch watcher-cb "bases" "components" "development"))