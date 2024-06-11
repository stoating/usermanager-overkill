(ns web.views.user.list
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.database.interface :as db]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn user-row
  [db user]
  (let [department-name (-> (db/user->department db user)
                            :name)
        this-user-row (str "user-" (user :xt/id))]
    [:tr {:id this-user-row}
     [:td (user :last-name)]
     [:td (user :first-name)]
     [:td (user :email)]
     [:td department-name]
     [:td
      [:a {:class ["cursor-pointer"]
           :title "Deletes user row"
           :hx-get (str (get rs/rs :user-delete) "/" (user :xt/id))
           :hx-trigger "click"
           :hx-target (str "#" this-user-row)
           :hx-swap "outerHTML"}
       "Delete"]
      [:. {:title "Increases change tracking"
           :hx-get (get rs/rs :default-changes-inc)
           :hx-trigger "click from:closest td"
           :hx-target "#changes-id"
           :hx-swap "outerHTML"}]]]))


(defn users-table [req]
  (tap> req)
  (let [db (get-in req [:app :db])
        users (db/get-users db)]
    [:table
     [:thead
      [:tr
       [:th "Last Name"]
       [:th "First Name"]
       [:th "Email"]
       [:th "Department"]
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