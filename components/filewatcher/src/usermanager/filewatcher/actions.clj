(ns usermanager.filewatcher.actions
  (:require [usermanager.filewatcher.reload :as reload]))


(defn eval-files! [cb]
  (let [eval-paths ["bases" "components"]
        on-eval nil]
    (println "eval-paths:" eval-paths)
    (println "on-eval:" on-eval)
    #_(println "callback content:" cb)
    (let [result (swap! reload/tracker-atom reload/refresh eval-paths)]
      (doseq [f on-eval]
        (f cb result))
      result)))