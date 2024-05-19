(ns usermanager.log.interface-test
  (:require [clojure.test :as test :refer [deftest is]]
            #_[usermanager.log.interface :as log]))

(println "in ns:" (str *ns*))

(deftest dummy-test
  (is (= 1 1)))