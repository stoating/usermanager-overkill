(ns usermanager.filewatcher.actions
  (:require [usermanager.filewatcher.reload :as reload]))

(println "in ns:" (str *ns*))

(defn eval-files! [cb]
  (let [eval-paths (get-in cb [:params :paths])
        on-eval nil]
    (println "eval-paths:" eval-paths)
    (println "on-eval:" on-eval)
    (println "callback content:" cb)
    (let [result (swap! reload/tracker-atom reload/refresh eval-paths)]
      (doseq [f on-eval]
        (f cb result))
      result)))

(println "end ns:" (str *ns*))