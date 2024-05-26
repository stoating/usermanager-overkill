(ns usermanager.web.routes
  (:require [portal.api :as p]
            [reitit.core :as rc]
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
    (let [resp (handler req)
          view (get-in req [:reitit.core/match :data :name])]
      (if (resp/response? resp)
        resp
        (view resp)))))


(def wrap-database
  {:name ::wrap-database
   :description "place the database at :db"
   :wrap (fn [handler db]
           (fn [req]
             (handler (assoc req :db db))))})


(defn app [db]
  (r/ring-handler
   (r/router
    [["/"      {:name home/homex
                :handler home/message-default}]
     ["/reset" {:name home/home
                :handler home/message-reset}]
     ["/login" views/login]]

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

(comment
  rc/Match)