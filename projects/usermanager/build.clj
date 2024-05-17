(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b]))

(println "in ns:" (str *ns*))

(def app 'usermanager)
(def main 'usermanager.web.core)
(def class-dir "target/classes")

(defn- uber-opts [opts]
  (assoc opts
         :main main
         :uber-file (format "target/%s-standalone.jar" app)
         :basis (b/create-basis {})
         :class-dir class-dir
         :src-dirs ["src"]
         :ns-compile [main]))

(defn uber
  "Build the uberjar."
  [opts]
  (b/delete {:path "target"})
  (let [opts (uber-opts opts)]
    (println "\nCopying source...")
    (b/copy-dir {:src-dirs ["src"] :target-dir class-dir})
    (println (str "\nCompiling " main "..."))
    (b/compile-clj opts)
    (println "\nBuilding JAR...")
    (b/uber opts))
  opts)

(println "end ns:" (str *ns*))
