(ns dev
  (:require [clojure.pprint :as pprint]
            [clojure.tools.namespace.track :as track]
            [integrant.core :as ig]
            [nextjournal.beholder :as bh]
            [usermanager.filewatcher.interface :as fw]
            [usermanager.web.core :as web]))

(println "in ns:" (str *ns*))

(comment
  ;; restart system
  (ig/halt! web/system)

  (ig/init web/config)


  ;; stop/start filewatcher
  fw/watcher

  (println "im doing this too")
  (bh/stop fw/watcher)

  ;; eval files a
  ;; {:type :modify, :path #object[sun.nio.fs.UnixPath 0x30604729 /workspaces/usermanager/bases/web/resources/web/home.clj]}
  (pprint/pprint (fw/eval-files! {:type :modify
                                  :path "/workspaces/usermanager"}))

  #_(def mydata {:type :modify, :path [sun.nio.fs.UnixPath 0x65d3d571 "/workspaces/usermanager/components/filewatcher/src/usermanager/filewatcher/actions.clj"]})

  (get mydata :path)

  (pprint/pprint fw/tracker-atom)
  (reset! fw/tracker-atom (atom (track/tracker)))
  ;
  (spit "out.edn" (with-out-str (pprint/pprint [:key "value"])))
  )

(println "end ns:" (str *ns*))