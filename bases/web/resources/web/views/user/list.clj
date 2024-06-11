(ns web.views.user.list
  (:require [portal.api :as p]
            [usermanager.database.queries :as q]
            [web.components.user-row :as user-row]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn body [req]
  (tap> req)
  (let [db (get-in req [:app :db])
        users (q/get-users db)]
    [:table
     [:thead
      [:tr
       [:th "Last Name"]
       [:th "First Name"]
       [:th "Email"]
       [:th "Department"]
       [:th "Delete"]]]
     [:tbody
      (for [count (range (count users))]
        (user-row/component db (users count)))]]))


(defn prepare-req [req]
  (assoc-in req [:app :html :body] body))

(comment
  (def mydata [{:department-id 3,
                :email "kong.mcbong@long.ng",
                :first-name "Funky",
                :xt/id #uuid "14c9714e-ca8b-4146-995b-5a0926ec80ed",
                :last-name "Kong"}
               {:department-id 1,
                :email "dooper.doopson@gdoop.com",
                :first-name "Honk",
                :xt/id #uuid "9fa3bf57-63af-4917-b20b-bfa86b8935cd",
                :last-name "McHonkerton"}
               {:department-id 2,
                :email "imacar@gmail.com",
                :first-name "Steve",
                :xt/id #uuid "b7cd8627-30a8-466f-8fc3-b35cdb714dfb",
                :last-name "McQueen"}
               {:department-id 3,
                :email "sean@worldsingles.com",
                :first-name "Sean",
                :xt/id #uuid "e02013b1-9f7c-45b3-8123-ff1df815abe1",
                :last-name "Corfield"}])
  (count mydata)
  )