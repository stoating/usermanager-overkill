(ns usermanager.web.routes
  (:require [portal.api :as p]
            [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [web.views.views :as views]
            [web.views.home :as home]))

(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(defn my-middleware
  [handler]
  (fn [req]
    (let [resp (handler req)]
      (if (resp/response? resp)
        resp
        (views/home resp)))))


(def wrap-database
  {:name ::wrap-database
   :description "place the database at :db"
   :wrap (fn [handler db]
           (fn [request]
             (handler (assoc request :db db))))})


(defn app [db]
  (r/ring-handler
   (r/router
    [["/"      views/home]
     ["/reset" {:handler home/reset-changes}]
     ["/login" views/login]]

    {:data {:db db
            :middleware [my-middleware
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
