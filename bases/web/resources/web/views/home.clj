(ns web.views.home
  (:require [web.views.-default :as default]
            #_[clojure.string :as str]))


(println "in ns:" (str *ns*))

#_(defn tw
  [classes]
  (->> (flatten classes)
       (remove nil?)
       (map name)
       (sort)
       (str/join " ")))

#_(def tw-input
  ["text-6xl"])

(def primary
  [:div
   [:h1 "Welcome to the User Managerxxxxxxx"]
   [:p "This is a simple web application that allows you to manage users."]])

(def links-component
  [:ul
   [:li [:a {:href "/"} "Home"]]
   [:li [:a {:href "/user/list"
             :title "View the list of users"} "Users"]]
   [:li [:a {:href "/user/form"
             :title "Fill out form to add new user"} "Add User"]]
   [:li [:a {:href "/reset"
             :title "Resets change tracking"} "Reset"]]])

(def changes-count 5)

(def changes-count-component
  [:div (str "Your have made " changes-count " change(s) since the last reset")])

(defn layout [db]
  (default/page
   {}
   [:body
    [:div
     [:h1 "User Manager"]
     links-component
     [:br]
     [:div {:id "primary"} primary]]
    changes-count-component]))