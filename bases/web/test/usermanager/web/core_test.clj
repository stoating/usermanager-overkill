(ns usermanager.web.core-test
  (:require [clojure.test :as test :refer :all]
            [usermanager.web.core :as core]))


(println "in ns:" (str *ns*))


(deftest dummy-test
  (is (= 1 1)))

(comment
  (println "in the comment"))
