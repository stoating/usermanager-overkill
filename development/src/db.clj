(ns db
  (:require [xtdb.api :as xt]
            [xtdb.client :as xtc]))

(comment
  (def mynode (xtc/start-client "http://localhost:6543"))
  (xt/status mynode)

  (xt/q mynode '(from :departments [*]))
  (xt/q mynode '(from :users [*]))
  (get (first (xt/q mynode '(-> (from :users [first-name])
                                (order-by first-name))))
       :first-name)

  ;; get department by id where id is 2
  (defn get-department-by-id [id]
    (-> (xt/q mynode '(-> (from :departments [name xt/id])
                          (return name)))
        (nth id)))

  (defn get-user-by-id [id]
    (-> (xt/q mynode '(-> (from :user [name xt/id])
                          (return name))))
    (-> (filter #(= (:xt/id %) id) mydata)
        first))

  (get-department-by-id 0)
  (get-user-by-id #uuid "c2409067-bca1-40e4-9fcf-c96469ae7855")

  (def mydata [{:department-id 4,
                :email "sean@worldsingles.com",
                :first-name "Sean",
                :xt/id #uuid "c2409067-bca1-40e4-9fcf-c96469ae7852",
                :last-name "Corfield"}
               {:department-id 5,
                :email "sean@worldsingles.com",
                :first-name "Sean",
                :xt/id #uuid "c2409067-bca1-40e4-9fcf-c96469ae7855",
                :last-name "Corfield"}])

  (-> (filter #(= (:department-id %) 4) mydata)
      first)

  (get-in (first [{:first-name "John"}]) [:first-name])
  (get (first [{:first-name "John"}]) :first-name)

  (def myval [{:department-id 4,
               :email "sean@worldsingles.com",
               :first-name "Sean",
               :xt/id #uuid "142bb771-ad2e-4eab-bfc6-59115ade65e9",
               :last-name "Corfield"}])

  (get (first myval) :first-name)
  )