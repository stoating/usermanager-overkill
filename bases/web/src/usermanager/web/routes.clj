(ns usermanager.web.routes
  (:require [portal.api :as p]
            [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [usermanager.web.controller.page :as page]
            [web.views.home :as home]))

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
    home/routes

    {:data {:middleware [my-middleware
                         par/parameters-middleware
                         wrap-keyword-params
                         [wrap-database db]]}})

   (r/routes
    (r/redirect-trailing-slash-handler)

    (r/create-resource-handler
     {:path "/"})

    (r/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})
      :method-not-allowed (constantly {:status 405 :body "Method not allowed"})
      :not-acceptable (constantly {:status 406 :body "Not acceptable"})}))))