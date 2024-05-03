(ns web.filewatcher
  (:require [nextjournal.beholder :as beholder]
            [usermanager.time.interface :as time]
            [usermanager.log.interface :as logging]
            [usermanager.web.reload :as reload]))

(defn eval-files! [cb]
  (let [eval-paths ["bases/web/resources"]
        on-eval nil]
    (println "eval-paths:" eval-paths)
    (println "on-eval:" on-eval)
    (println "callback content:" cb)
    (let [result (swap! reload/tracker-atom reload/refresh eval-paths)]
      (doseq [f on-eval]
        (f cb result))
      result)))


(def time-since-last-save (atom (java.util.Date.)))


(defn watcher-cb-actions [cb]
  (println "start watcher callback actions")
  (eval-files! cb)
  (println "finish watcher callback actions"))


(defn watcher-cb [cb]
  (println "watcher callback triggered.")
  (if (time/elapsed? @time-since-last-save :now 2 :seconds)
    ((Thread/sleep 200)
     (logging/catchall-verbose (watcher-cb-actions cb))
     (reset! time-since-last-save (java.util.Date.)))
    (println "spamming save, skip watcher callback actions")))


(def watcher
  (beholder/watch watcher-cb "bases/web/resources"))