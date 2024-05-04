(ns usermanager.log.interface-test
  (:require [clojure.test :as test :refer :all]
            [usermanager.log.interface :as log]))

(println "in ns:" (str *ns*))

(deftest dummy-test
  (is (= 1 1)))
