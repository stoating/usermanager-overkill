(ns web.views.home
  (:require [portal.api :as p]
            [usermanager.database.interface :as db]
            [web.layouts.default :as default]
            [rum.core :as rum]
            [ring.util.response :as rr]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def changes
  "Count the number of changes (since the last reload)."
  (atom 5))


(defn message-default
  [req]
  (tap> req)
  (assoc-in req [:params :message]
            (str "Welcome to the User Manager application demo! "
                 "This uses just Aero, Beholder, Integrant, Polylith, Portal, Reitit, Rum, XTDB, Babashka, HTMX, Tailwind, Docker, and Devcontainers.")))


(defn to-html [hiccup]
  (-> hiccup
      rum/render-static-markup
      str
      rr/response))


(defn my-changes [changes]
  [:div {:id "my-changes-id"}
   (str "Your have made " changes " change(s) since the last reset")])


(defn message-reset [_]
  (reset! changes 0)
  (to-html (my-changes @changes)))


(def my-message
  [:button {:hx-get "/test"
            :hx-trigger "click"
            :hx-swap "outerHTML"}
   "im the original message"])


(defn message-toggle-to [_]
  (-> my-message
      (assoc-in [1 :hx-get] "/test2")
      (assoc 2 "im the new message")
      to-html))


(defn message-toggle-back [_]
  (to-html my-message))


(defn layout [req]
  (let [db (:db req)
        users (db/get-users db)
        message (get-in req [:params :message])]
    #_(tap> req)
    (default/page
     [:body
      [:div
       [:h1 (get (first users) :first-name)]
       [:ul
        [:li [:a {:href "/"} "Home"]]
        [:li [:a {:href "/user/list"
                  :title "View the list of users"} "Users"]]
        [:li [:a {:href "/user/form"
                  :title "Fill out form to add new user"} "Add User"]]
        [:li [:button {:hx-get "/reset"
                       :hx-trigger "click"
                       :hx-target "#my-changes-id"
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


(defn render-page [req]
  (let [data (assoc-in req [:params :changes] @changes)
        html (str (rum/render-static-markup (layout data)))]
    (rr/response html)))
