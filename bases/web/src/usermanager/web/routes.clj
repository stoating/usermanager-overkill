(ns usermanager.web.routes
  (:require [reitit.ring :as ring]
            [reitit.ring.middleware.parameters :as parameters]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as resp]
            [web.views.home :as home]
            [web.views.login :as login]))

(defn app []
  (ring/ring-handler
   (ring/router
    [["/" {:get {:handler (fn [_] (resp/response (str home/layout)))}}]
     ["/login" {:get {:handler (fn [_] (resp/response (str login/layout)))}}]]
    {:data {:middleware [parameters/parameters-middleware
                         wrap-keyword-params]}})
   (ring/routes
    (ring/create-resource-handler
     {:path "/"})
    (ring/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})}))))