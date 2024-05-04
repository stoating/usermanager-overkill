(ns usermanager.filewatcher.interface-test
  (:require [clojure.test :as test :refer :all]
            [usermanager.filewatcher.interface :as filewatcher]))

(println "in ns:" (str *ns*))

(deftest dummy-test
  (is (= 1 1)))
