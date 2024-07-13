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
  (let [{:keys [app path-params]} req
        {db :db} app
        {id :id} path-params

        user (if id (db/get-user-by-id db (parse-uuid id)) "")
        {:keys [first-name last-name email department-id]} user

        departments (db/get-departments db)

        post-fn (if id
                  (str (get rs/rs :user-update) "/" id)
                  (get rs/rs :user-add))
        button-text (if id
                      "Update User"
                      "Add User")]

    [:form {:class ["w-full bg-gray-600 rounded-lg shadow-xl p-2"]
            :id "user-form"}
     [:. {:class ["flex flex-wrap"]}

      [:. {:class ["w-full md:w-1/2 px-3 py-2"]}
       [:label {:class label-css
                :for "first_name"} "First Name"]
       [:input {:class input-css
                :type "text" :name "first_name" :value first-name}]]

      [:. {:class ["w-full md:w-1/2 px-3 md:py-2"]}
       [:label {:class label-css
                :for "last_name"} "Last Name"]
       [:input {:class input-css
                :type "text" :name "last_name" :value last-name}]]

      [:. {:class ["w-full px-3 py-2"]}
       [:label {:class label-css
                :for "email"} "Email"]
       [:input {:class input-css
                :type "text" :name "email" :value email}]]

      [:. {:class ["w-full md:w-2/3 px-3 md:pt-2 md:pb-3"]}
       [:label {:class label-css
                :for "department_id"} "Department"]
       [:. {:class ["relative"]}
        [:select {:class input-css
                  :type "number" :name "department_id"}
         (for [i (range (count departments))]
           (let [cur_dep (departments i)]
             (if (= (cur_dep :xt/id) department-id)
               [:option {:value (cur_dep :xt/id) :selected true} (cur_dep :name)]
               [:option {:value (cur_dep :xt/id)} (cur_dep :name)])))]]]

      [:. {:class ["w-full md:w-1/3 md:pt-8 pt-7 px-3 py-2"]}
       [:button {:class button-css
                 :type "submit"
                 :hx-post post-fn
                 :hx-trigger "click"
                 :hx-target "#user-form"
                 :hx-swap "outerHTML"}
        button-text
        [:. {:title "Increases change tracking"
             :hx-get (get rs/rs :default-changes-inc)
             :hx-trigger "click from:closest button"
             :hx-target "#changes-id"
             :hx-swap "outerHTML"}]]]]]))


(defn user-add [req]
  (let [db (get-in req [:app :db])
        params (util/kebabify (get-in req [:params]))]
    (db/insert-user db {:first-name (params :first-name)
                        :last-name (params :last-name)
                        :email (params :email)
                        :department-id (->> (params :department-id)
                                            (Integer/parseInt))})
    (util/hiccup->html-resp (user-form req) {"HX-Redirect" (get rs/rs :user-list)})))


(defn user-update [req]
  (let [db (get-in req [:app :db])
        params (util/kebabify (get-in req [:params]))
        id (get-in req [:path-params :id])]
    (db/update-user db {:first-name (params :first-name)
                        :last-name (params :last-name)
                        :email (params :email)
                        :department-id (->> (params :department-id)
                                            (Integer/parseInt))
                        :xt/id (parse-uuid id)})
    (util/hiccup->html-resp (user-form req) {"HX-Redirect" (get rs/rs :user-list)})))


(defn prepare-req [req]
  (assoc-in req [:app :html :body] user-form))
