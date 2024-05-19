(ns dev
  (:require [clojure.pprint :as pprint]
            [clojure.tools.namespace.track :as track]
            [integrant.core :as ig]
            [usermanager.web.core :as web]
            [portal.api :as p]))

(println "in ns:" (str *ns*))

(comment
  ;; restart system
  (ig/halt! web/system)

  (ig/init web/config)

  #_(defn start-filewatcher []
    '(fw/time-since-last-save)
    fw/watcher)

  #_(start-filewatcher)
  ;; start filewatcher
  ;; note: you should not have to do this
  #_fw/watcher
  ;; stop filewatcher
  #_(bh/stop fw/watcher)

  #_(defn clear-tracker-atom []
    (reset! fw/tracker-atom (atom (track/tracker))))
  #_(clear-tracker-atom)
  #_(tap> fw/tracker-atom)
  (def portal (p/open))
  (add-tap #'p/submit)

  ;; eval files a
  ;; {:type :modify, :path #object[sun.nio.fs.UnixPath 0x30604729 /workspaces/usermanager/bases/web/resources/web/home.clj]}
  #_(pprint/pprint (fw/eval-files! {:type :modify
                                  :path "/workspaces/usermanager"}))

  #_(def mydata {:type :modify, :path [sun.nio.fs.UnixPath 0x65d3d571 "/workspaces/usermanager/components/filewatcher/src/usermanager/filewatcher/actions.clj"]})

  (get mydata :path)

  #_(pprint/pprint fw/tracker-atom)
  #_(reset! fw/tracker-atom (atom (track/tracker)))
  ;
  (spit "out.edn" (with-out-str (pprint/pprint [:key "value"])))
  )

(println "end ns:" (str *ns*))