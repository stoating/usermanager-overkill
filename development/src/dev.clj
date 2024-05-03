(ns dev
  (:require [usermanager.web.core :as web]
            [web.filewatcher :as fw]
            [integrant.core :as ig]
            [nextjournal.beholder :as bh]))

(ig/halt! web/system)
(ig/init web/config)

fw/watcher
(bh/stop fw/watcher)

