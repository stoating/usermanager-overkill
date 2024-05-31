(ns web.views.home
  (:require [portal.api :as p]
            [usermanager.database.interface :as db]
            [usermanager.web.controller.page :as page]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def changes
  "Count the number of changes (since the last reload)."
  (atom 5))


(defn my-changes [changes]
  [:div {:id "changes-id"}
   (str "Your have made " changes " change(s) since the last reset")])


(defn changes-reset [_]
  (reset! changes 0)
  (page/to-html (my-changes @changes)))


(def my-message
  [:button {:hx-get "home/message-toggle"
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "im the original message"])


(defn message-toggle [_]
  (-> my-message
      (assoc-in [1 :hx-get] "home/message-toggle-reset")
      (assoc 2 "im the new message")
      page/to-html))


(defn message-toggle-reset [_]
  (page/to-html my-message))


(defn body [_]
  (fn [req]
    (let [db (get-in req [:app :db])
          users (db/get-users db)
          message (get-in req [:app :params :message])]
      [:body
       [:div
        [:h1 (get (first users) :first-name)]
        [:ul
         [:li [:a {:href "/"} "Home"]]
         [:li [:a {:href "/user/list"
                   :title "View the list of users"} "Users"]]
         [:li [:a {:href "/user/form"
                   :title "Fill out form to add new user"} "Add User"]]
         [:li [:button {:hx-get "/changes-reset"
                        :hx-trigger "click"
                        :hx-target "#changes-id"
                        :hx-swap "outerHTML"
                        :title "Resets change tracking"}
               "Reset"]]]
        [:br]
        [:div {:id "primary"}
         [:div
          [:h1 "Welcome to the User Managerx"]
          [:p "This is a simple web application that allows you to manage users."]]]]
       (my-changes @changes)
       [:div (str "Message: " message)]
       my-message])))


(def home
  (fn [req]
    (-> req
        (assoc-in [:app :params :changes] @changes)
        (assoc-in [:app :params :message]
                  (str "Welcome to the User Manager application demo! "
                       "This uses just Aero, Beholder, Integrant, Polylith, Portal, Reitit, Rum, XTDB, Babashka, HTMX, Tailwind, Docker, and Devcontainers."))
        (assoc-in [:app :html :body] (body req)))))