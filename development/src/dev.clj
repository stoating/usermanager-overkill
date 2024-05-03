(ns dev
  (:require [usermanager.web.core :as web]
            [usermanager.filewatcher.interface :as fw]
            [integrant.core :as ig]
            [nextjournal.beholder :as bh]))

(ig/halt! web/system)
(ig/init web/config)

fw/watcher
(bh/stop fw/watcher)

