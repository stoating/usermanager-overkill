(ns usermanager.time.interface
  (:require [usermanager.time.core :as core]))

(defn elapsed? [t1 t2 x unit]
  (core/elapsed? t1 t2 x unit))
