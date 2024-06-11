(ns db
  (:require [clojure.pprint :as pp]
            [usermanager.database.seed :as seed]
            [usermanager.database.queries :as q]
            [usermanager.database.transactions :as tx]
            [xtdb.api :as xt]
            [xtdb.client :as xtc]))

(comment
  ;; create connection to db
  (def db (xtc/start-client "http://localhost:6543"))
  (xt/status db)

  ;; populate users
  ;; the print ensures the dom updates correctly?
  (def populate-users
    (do (doseq [cur-user seed/users-seed]
          (tx/insert-user db cur-user))
        (pp/pprint (xt/q db '(from :users [*])))))

  ;; debug
  (pp/pprint (xt/q db '(from :departments [*])))
  (pp/pprint (xt/q db '(from :users [*])))

  (q/get-department-by-id db 2)
  (get (first (xt/q db '(-> (from :users [first-name])
                                (order-by first-name))))
       :first-name)

  ;; get department by id where id is 2
  (defn get-department-by-id [id]
    (-> (xt/q db '(-> (from :departments [name xt/id])
                          (return name)))
        (nth id)))

  (defn get-user-by-id [db id]
    (->> (xt/q db '(from :users [*]))
         (filter #(= (:xt/id %) id))
         first))

  (xt/submit-tx db [[]])

  (get-user-by-id db #uuid "898ab528-71ec-4aaa-aa28-238e8a409f7a")

  (db)
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

  (-> (filter (fn [c] (= (:department-id c) 4)) mydata)
      first)

  (defn insert-department [db name id]
    (xt/submit-tx db [[:put-docs :departments
                       {:xt/id id
                        :name name}]]))


  (if (nil? (get-max-id-departments db))
    (insert-department db 0 "Engineering")
    (-> (get-max-id-departments db)
        inc
        (insert-department db "Engineering")))


  (defn get-max-id-departments [db]
    (-> (xt/q db '(-> (from :departments [xt/id])
                      (order-by {:val xt/id :dir :desc})
                      (limit 1)))
        (get-in [0 :xt/id])))

  (get-max-id-departments db)

  (get-in (first [{:first-name "John"}]) [:first-name])
  (get (first [{:first-name "John"}]) :first-name)

  (def myval [{:department-id 4,
               :email "sean@worldsingles.com",
               :first-name "Sean",
               :xt/id #uuid "142bb771-ad2e-4eab-bfc6-59115ade65e9",
               :last-name "Corfield"}])

  (get (first myval) :first-name)
  )