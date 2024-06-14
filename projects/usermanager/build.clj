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
         :src-dirs ["src" "resources"]
         :ns-compile [main]))

(defn uber
  "Build the uberjar."
  [opts]
  (b/delete {:path "target"})
  (let [opts (uber-opts opts)]
    (println "\nCopying src and resources dirs to" class-dir "...")
    (b/copy-dir {:src-dirs ["src" "resources"] :target-dir class-dir})

    (println (str "\nCompiling " main "..."))
    (b/compile-clj opts)

    (println "\nBuilding JAR...")
    (b/uber opts))
  opts)
