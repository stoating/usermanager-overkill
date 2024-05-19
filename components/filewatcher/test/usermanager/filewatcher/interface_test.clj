(ns usermanager.filewatcher.interface-test
  (:require [clojure.test :as test :refer [deftest is]]
            #_[usermanager.filewatcher.interface :as filewatcher]))

(println "in ns:" (str *ns*))

(deftest dummy-test
  (is (= 1 1)))