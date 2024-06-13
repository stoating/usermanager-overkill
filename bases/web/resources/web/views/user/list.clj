(ns web.views.user.list
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.database.interface :as db]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def table-button-css
  ["cursor-pointer"
   "px-2"
   "text-center"
   "font-bold"
   "bg-gray-400"
   "hover:bg-gray-800"
   "hover:text-white"
   "transition"
   "duration-200"])


(defn user-row
  [db user]
  (let [department-name (-> (db/user->department db user)
                            :name)
        this-user-row (str "user-" (user :xt/id))]
    [:tr {:id this-user-row}
     [:td {:class ["px-2 text-left font-bold bg-gray-400"]} (user :last-name)]
     [:td {:class ["px-2 text-left font-bold bg-gray-400"]} (user :first-name)]
     [:td {:class ["px-2 text-left font-bold bg-gray-400"]} (user :email)]
     [:td {:class ["px-2 text-right font-bold bg-gray-400"]} department-name]
     [:td {:class table-button-css
           :title "Edit user row"
           :hx-get (str (get rs/rs :user-form) "/" (user :xt/id))
           :hx-push-url "true"
           :hx-target "body"
           :hx-trigger "click"} "Edit"]
     [:td {:class table-button-css
           :title "Deletes user row"
           :hx-get (str (get rs/rs :user-delete) "/" (user :xt/id))
           :hx-trigger "click"
           :hx-target (str "#" this-user-row)
           :hx-swap "outerHTML"} "X"
      [:. {:title "Increases change tracking"
           :hx-get (get rs/rs :default-changes-inc)
           :hx-trigger "click from:closest td"
           :hx-target "#changes-id"
           :hx-swap "outerHTML"}]]]))


(def table-css
  ["w-full"
   "table-auto"
   "rounded-lg"
   "shadow-xl"
   "border-separate"
   "border-tools-table-outline"
   "border-gray-700"
   "border-4"])


(defn users-table [req]
  (let [db (get-in req [:app :db])
        users (db/get-users db)]
    [:table {:class table-css}
     [:thead {:class ["text-white bg-gray-600 text-center"]}
      [:tr
       [:th "Last Name"]
       [:th "First Name"]
       [:th "Email"]
       [:th "Department"]
       [:th "Edit"]
       [:th "Delete"]]]
     [:tbody
      (for [i (range (count users))]
        (user-row db (users i)))]]))


(defn user-delete [req]
  (let [db (get-in req [:app :db])
        id (get-in req [:path-params :id])]
    (db/delete-user-by-id db (parse-uuid id))
    (util/hiccup->html-resp [:<>])))


(defn prepare-req [req]
  (assoc-in req [:app :html :body] users-table))