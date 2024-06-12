(ns web.components.navbar
  (:require [routes :as rs]))


(println "in ns:" (str *ns*))

(def nav-css
  [;; layout
   "flex"
   "justify-center"

   ;; spacing
   "py-2"
   "space-x-2"

   ;; backgound
   "bg-gray-200"
   "shadow-md"])

(def nav-a-css
  [;; spacing
   "p-4"

   ;; formatting
   "rounded-md"
   "text-gray-800"

   ;; static effects
   "cursor-pointer"

   ;; dynamic effects
   "hover:bg-gray-700"
   "hover:py-3"
   "hover:text-white"

   ;; timing
   "transition"
   "duration-300"])

(def component
  [:nav {:class nav-css}
   [:a {:class nav-a-css
        :href (get rs/rs :home)} "Home"]
   [:a {:class nav-a-css
        :href (get rs/rs :user-list)
        :title "View the list of users"} "Users"]
   [:a {:class nav-a-css
        :href (get rs/rs :user-form)
        :title "Fill out form to add new user"} "Add User"]
   [:a {:class nav-a-css
        :hx-get (get rs/rs :default-changes-reset)
        :hx-trigger "click"
        :hx-target "#changes-id"
        :hx-swap "outerHTML"
        :title "Resets change tracking"} "Reset"]])