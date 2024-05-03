(ns dev
  (:require [integrant.core :as ig]
            [nextjournal.beholder :as bh]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.core :as web]))

(ig/halt! web/system)
(ig/init web/config)

fw/watcher
(bh/stop fw/watcher)

