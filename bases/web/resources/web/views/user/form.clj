(ns web.views.user.form
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.database.interface :as db]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def button-css
  ["block"
   "w-full"
   "py-3"
   "rounded"
   "border"
   "border-gray-800"
   "shadow-xl"
   "bg-teal-300"
   "text-sm"
   "font-bold"
   "uppercase"
   "transition"
   "duration-200"
   "hover:bg-teal-700"
   "hover:border-gray-400"
   "hover:text-white"])


(def label-css
  ["block"
   "uppercase"
   "tracking-wide"
   "text-white"
   "text-xs"
   "font-bold"
   "py-1"])


(def input-css
  ["appearance-none"
   "block"
   "w-full"
   "bg-gray-200"
   "text-gray-700"
   "border"
   "border-gray-700"
   "rounded"
   "py-3"
   "px-4"
   "leading-tight"
   "focus:outline-none"
   "focus:bg-white"])


(defn user-form [req]
  (let [db (get-in req [:app :db])
        departments (db/get-departments db)]
    [:form {:class ["w-full bg-gray-600 rounded-lg shadow-xl p-2"]
            :id "user-form"}
     [:. {:class ["flex flex-wrap"]}

      [:. {:class ["w-full md:w-1/2 px-3 py-2"]}
       [:label {:class label-css
                :for "first_name"} "First Name"]
       [:input {:class input-css
                :type "text" :name "first_name" :value ""}]]

      [:. {:class ["w-full md:w-1/2 px-3 md:py-2"]}
       [:label {:class label-css
                :for "last_name"} "Last Name"]
       [:input {:class input-css
                :type "text" :name "last_name" :value ""}]]

      [:. {:class ["w-full px-3 py-2"]}
       [:label {:class label-css
                :for "email"} "Email"]
       [:input {:class input-css
                :type "text" :name "email" :value ""}]]

      [:. {:class ["w-full md:w-2/3 px-3 md:pt-2 md:pb-3"]}
       [:label {:class label-css
                :for "department_id"} "Department"]
       [:. {:class ["relative"]}
        [:select {:class input-css
                  :type "number" :name "department_id" :value ""}
         (for [i (range (count departments))]
           (let [department (departments i)]
             [:option {:value (department :xt/id)} (department :name)]))]]]

      [:. {:class ["w-full md:w-1/3 md:pt-8 pt-7 px-3 py-2"]}
       [:button {:class button-css
                 :type "submit"
                 :hx-post (get rs/rs :user-add)
                 :hx-trigger "click"
                 :hx-target "#user-form"
                 :hx-swap "outerHTML"}
        "Add User"
        [:. {:title "Increases change tracking"
             :hx-get (get rs/rs :default-changes-inc)
             :hx-trigger "click from:closest button"
             :hx-target "#changes-id"
             :hx-swap "outerHTML"}]]]]]))


(defn user-add [req]
  (let [db (get-in req [:app :db])
        params (get-in req [:params])]
    (db/insert-user db {:first_name (params :first_name)
                        :last_name (params :last_name)
                        :email (params :email)
                        :department_id (->> (params :department_id)
                                            (Integer/parseInt))})
    (util/hiccup->html-resp (user-form req))))


(defn prepare-req [req]
  (assoc-in req [:app :html :body] user-form))


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