(ns usermanager.time.interface-test
  (:require [clojure.test :as test :refer [deftest is]]
            #_[usermanager.time.interface :as time]))

(println "in ns:" (str *ns*))

(deftest dummy-test
  (is (= 1 1)))
