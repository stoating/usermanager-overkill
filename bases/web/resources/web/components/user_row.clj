(ns web.components.user-row
  (:require [portal.api :as p]
            [routes :as rs]
            [usermanager.database.relations :as dbr]
            [usermanager.database.transactions :as dbtx]
            [usermanager.web.controller.util :as util]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn component
  [db user]
  (let [department-name (-> (dbr/user->department db user)
                            :name)]
    [:tr {:id (str "user-" (user :xt/id))}
     [:td (user :last-name)]
     [:td (user :first-name)]
     [:td (user :email)]
     [:td department-name]
     [:td {:class ["cursor-pointer"]}
      [:a {:hx-get (str (get rs/rs :user-delete) "/" (user :xt/id))
           :hx-trigger "click"
           :hx-target (str "#user-" (user :xt/id))
           :hx-swap "outerHTML"
           :title "Deletes user row"}
       "Delete"]
      [:. {:hx-get (get rs/rs :default-changes-inc)
           :hx-trigger "click from:closest td"
           :hx-target "#changes-id"
           :hx-swap "outerHTML"
           :title "Increases change tracking"}]]]))


(defn delete-user [req]
  (let [db (get-in req [:app :db])
        id (get-in req [:path-params :id])]
    (tap> "delete-user")
    (tap> id)
    (dbtx/delete-user-by-id db (parse-uuid id))
    (util/hiccup->html-resp [:<>])))
