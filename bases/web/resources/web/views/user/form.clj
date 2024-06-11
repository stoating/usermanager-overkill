(ns web.views.user.form
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.database.interface :as db]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn user-add-form [req]
  (let [db (get-in req [:app :db])
        departments (db/get-departments db)]
    [:form {:id "user-add-form"}

     [:div
      [:label {:for "first_name"} "First Name"]
      [:input {:type "text" :name "first_name" :value ""}]]

     [:div
      [:label {:for "last_name"} "Last Name"]
      [:input {:type "text" :name "last_name" :value ""}]]

     [:div
      [:label {:for "email"} "Email"]
      [:input {:type "text" :name "email" :value ""}]]

     [:div
      [:label {:for "department_id"} "Department"]
      [:select {:type "number" :name "department_id" :value ""}
       [:option {:value "" :selected "selected"} ""]
       (for [i (range (count departments))]
         (let [department (departments i)]
           [:option {:value (department :xt/id)} (department :name)]))]]

     [:button {:type "submit"
               :hx-post (get rs/rs :user-add)
               :hx-trigger "click"
               :hx-target "#user-add-form"
               :hx-swap "outerHTML"}
      "Add User"
      [:. {:title "Increases change tracking"
           :hx-get (get rs/rs :default-changes-inc)
           :hx-trigger "click from:closest button"
           :hx-target "#changes-id"
           :hx-swap "outerHTML"}]]]))


(defn user-add [req]
  (let [db (get-in req [:app :db])
        params (get-in req [:params])]
    (db/insert-user db {:first_name (params :first_name)
                        :last_name (params :last_name)
                        :email (params :email)
                        :department_id (->> (params :department_id)
                                            (Integer/parseInt))})
    (util/hiccup->html-resp (user-add-form req))))



(defn prepare-req [req]
  (assoc-in req [:app :html :body] user-add-form))


(comment
  (require '[xtdb.client :as xtc]
           '[xtdb.api :as xt]
           '[clojure.pprint :as pp])
  ;; create connection to db
  (def db (xtc/start-client "http://localhost:6543"))
  (xt/status db)

  (Integer/parseInt "1")

  (pp/pprint (db/get-departments db))
  (pp/pprint (db/get-users db))
  (count (db/get-departments db))
  (for [count (range (count (db/get-departments db)))]
    (println (get ((db/get-departments db) count) :name)))
  (db/insert-user db {:first_name "bonk"
                      :last_name "bonk"
                      :email "email@email.com"
                      :department_id 1})

  )