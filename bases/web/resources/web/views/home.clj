(ns web.views.home
  (:require [portal.api :as p]
            [usermanager.database.interface :as db]
            [web.views.-default :as default]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def changes
  "Count the number of changes (since the last reload)."
  (atom 5))


(defn reset-changes
  [req]
  (reset! changes 0)
  (assoc-in req [:params :message] "The change tracker has been reset."))


(defn layout [req]
  (let [db (:db req)
        users (db/get-users db)]
    (tap> req)
    (default/page
     {}
     [:body
      [:div
       [:h1 (get (first users) :first-name)]
       [:ul
        [:li [:a {:href "/"} "Home"]]
        [:li [:a {:href "/user/list"
                  :title "View the list of users"} "Users"]]
        [:li [:a {:href "/user/form"
                  :title "Fill out form to add new user"} "Add User"]]
        [:li [:a {:href "/reset"
                  :title "Resets change tracking"} "Reset"]]]
       [:br]
       [:div {:id "primary"}
        [:div
         [:h1 "Welcome to the User Managerxx"]
         [:p "This is a simple web application that allows you to manage users."]]]]
      [:div (str "Your have made " @changes " change(s) since the last reset")]])))