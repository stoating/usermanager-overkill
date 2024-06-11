(ns usermanager.database.queries
  (:require [xtdb.api :as xt]))


(defn get-department-by-id [db id]
  (-> (xt/q db '(-> (from :departments [name xt/id])
                    (return name)))
      (nth id)))


#_(defn get-departments [db]
  (xt/q db '(from :departments [*])))


(defn get-departments-max-id [db]
  (-> (xt/q db '(-> (from :departments [xt/id])
                    (order-by {:val xt/id :dir :desc})
                    (limit 1)))
      (get-in [0 :xt/id])))


#_(defn get-user-by-id [db id]
  (->> (xt/q db '(from :users [*]))
       (filter (fn [user] (= (:xt/id user) id)))
       first))


(defn get-users [db]
  (xt/q db '(-> (from :users [*])
                (order-by {:val last-name :dir :asc}))))


(comment
  (require '[clojure.pprint :as pp])
  (def mydata [{:department-id 3,
                :email "kong.mcbong@long.ng",
                :first-name "Funky",
                :xt/id #uuid "14c9714e-ca8b-4146-995b-5a0926ec80ed",
                :last-name "Kong"}
               {:department-id 1,
                :email "dooper.doopson@gdoop.com",
                :first-name "Honk",
                :xt/id #uuid "9fa3bf57-63af-4917-b20b-bfa86b8935cd",
                :last-name "McHonkerton"}
               {:department-id 2,
                :email "imacar@gmail.com",
                :first-name "Steve",
                :xt/id #uuid "b7cd8627-30a8-466f-8fc3-b35cdb714dfb",
                :last-name "McQueen"}
               {:department-id 3,
                :email "sean@worldsingles.com",
                :first-name "Sean",
                :xt/id #uuid "e02013b1-9f7c-45b3-8123-ff1df815abe1",
                :last-name "Corfield"}])

  (pp/pprint (sort-by :last-name mydata))
  )
