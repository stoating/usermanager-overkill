(ns usermanager.web.routes
  (:require [portal.api :as p]
            [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [routes :as rs]
            [usermanager.web.controller.page :as page]
            [web.components.changes-counter :as changes-counter]
            [web.components.message-toggle :as message-toggle]
            [web.components.navbar :as navbar]
            [web.views.home :as home]
            [web.views.user.form :as user-form]
            [web.views.user.list :as user-list]))

(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn req-resp-interceptor
  [handler]
  (fn [req]
    (let [resp (handler req)]
      (if (resp/response? resp)
        resp
        (page/render-page resp)))))


(def wrap-database
  {:name ::wrap-database
   :description "place the database at :app :db"
   :wrap (fn [handler db]
           (fn [req]
             (handler (assoc-in req [:app :db] db))))})


(def wrap-state
  {:name ::wrap-state
   :description "place the state at :app :state"
   :wrap (fn [handler state]
           (fn [req]
             (handler (assoc-in req [:app :state] state))))})


(defn app [state db]
  (r/ring-handler
   (r/router
    ;; views
    [[(get rs/rs :home)
      {:name ::home :get home/prepare-req}]
     [(get rs/rs :user-list)
      {:name ::user-list :get user-list/prepare-req}]
     [(get rs/rs :user-form)
      {:name ::user-form :get user-form/prepare-req}]
     [(str (get rs/rs :user-form) "/:id")
      {:name ::user-form-id :get user-form/prepare-req}]

     ;; actions
     ;; change counter
     [(get rs/rs :default-changes-inc)
      {:handler changes-counter/changes-inc}]
     [(get rs/rs :default-changes-reset)
      {:handler changes-counter/changes-reset}]

     ;; message toggle
     [(get rs/rs :default-message-toggle)
      {:handler message-toggle/message-toggle}]
     [(get rs/rs :default-message-toggle-reset)
      {:handler message-toggle/message-toggle-reset}]

     ;; user actions
     [(str (get rs/rs :user-add))
      {:handler user-form/user-add}]
     [(str (get rs/rs :user-delete) "/:id")
      {:handler user-list/user-delete}]
     [(str (get rs/rs :user-update) "/:id")
      {:handler user-form/user-update}]]

    {:data {:middleware [req-resp-interceptor
                         par/parameters-middleware
                         wrap-keyword-params
                         [wrap-database db]
                         [wrap-state state]]}})

   (r/routes
    (r/redirect-trailing-slash-handler)

    (r/create-resource-handler
     {:path "/"})

    (r/create-default-handler
     {:not-found          (constantly {:status 404 :body "Not found"})
      :method-not-allowed (constantly {:status 405 :body "Method not allowed"})
      :not-acceptable     (constantly {:status 406 :body "Not acceptable"})}))))

(comment
  (require '[clojure.pprint :as pprint])
  (pprint/pprint home/routes)
  (pprint/pprint navbar/routes)
  (pprint/pprint (apply merge home/routes navbar/routes)))