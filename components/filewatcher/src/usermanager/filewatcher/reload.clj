(ns usermanager.filewatcher.reload
  (:require [clojure.repl :as repl]
            [clojure.tools.namespace.dir :as dir]
            [clojure.tools.namespace.find :as find]
            [clojure.tools.namespace.reload :as reload]
            [clojure.tools.namespace.repl :as ns-repl]
            [clojure.tools.namespace.track :as track]))


(println "in ns:" (str *ns*))


(defonce tracker-atom (atom (track/tracker)))


(def remove-disabled #'ns-repl/remove-disabled)


(defn- print-pending-reloads [tracker]
  (when-let [r (seq (::track/load tracker))]
    (prn :reloading r)))


(defn print-and-return [tracker]
  (if-let [e (::reload/error tracker)]
    (do (when (thread-bound? #'*e)
          (set! *e e))
        (prn :error-while-loading (::reload/error-ns tracker))
        (repl/pst e)
        e)
    :ok))


(defn refresh [tracker dirs]
  (let [tracker-new (apply dir/scan-dirs tracker dirs {:platform find/clj})
        tracker-new (remove-disabled tracker-new)]
    (print-pending-reloads tracker-new)
    (let [tracker-new (reload/track-reload (assoc tracker-new ::track/unload []))]
      (print-and-return tracker-new)
      tracker-new)))