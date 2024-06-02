(ns usermanager.web.routes
  (:require [portal.api :as p]
            [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [routes :as rs]
            [usermanager.web.controller.page :as page]
            [web.components.message-toggle :as message-toggle]
            [web.components.navbar :as navbar]
            [web.views.home :as home]
            [web.views.user.form :as user-form]
            [web.views.user.list :as user-list]))

(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn my-middleware
  [handler]
  (fn [req]
    (tap> "req")
    (tap> req)
    (let [resp (handler req)]
      (tap> "resp")
      (tap> resp)
      (if (resp/response? resp)
        resp
        (page/render-page resp)))))


(def wrap-database
  {:name ::wrap-database
   :description "place the database at :app :db"
   :wrap (fn [handler db]
           (fn [req]
             (handler (assoc-in req [:app :db] db))))})


(defn app [db]
  (r/ring-handler
   (r/router
    [[(get rs/rs :home)
      {:name ::home :get home/home}]
     [(get rs/rs :home-changes-reset)
      {:handler home/changes-reset}]
     [(get rs/rs :home-message-toggle)
      {:handler message-toggle/message-toggle}]
     [(get rs/rs :home-message-toggle-reset)
      {:handler message-toggle/message-toggle-reset}]
     [(get rs/rs :user-form)
      {:name ::user-form :get user-form/user-form}]
     [(get rs/rs :user-list)
      {:name ::user-list :get user-list/user-list}]]
    {:data {:middleware [my-middleware
                         par/parameters-middleware
                         wrap-keyword-params
                         [wrap-database db]]}})

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