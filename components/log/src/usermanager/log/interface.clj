(ns usermanager.log.interface
  (:require [usermanager.log.core :as core]))

(defn catchall-verbose [& body]
  (core/catchall-verbose body))