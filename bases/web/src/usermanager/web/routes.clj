(ns usermanager.web.routes
  (:require [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as rr]
            [web.views.home :as home]
            [web.views.login :as login]))

(println "in ns:" (str *ns*))

(defn app []
  (r/ring-handler
   (r/router
    [["/"      {:get {:handler (fn [_] (rr/response (str home/layout)))}}]
     ["/login" {:get {:handler (fn [_] (rr/response (str login/layout)))}}]]

    {:data {:middleware [par/parameters-middleware
                         wrap-keyword-params]}})
   (r/routes
    (r/create-resource-handler
     {:path "/"})
    (r/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})}))))

(println "end ns:" (str *ns*))