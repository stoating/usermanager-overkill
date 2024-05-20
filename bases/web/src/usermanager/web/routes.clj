(ns usermanager.web.routes
  (:require [reitit.ring :as r]
            [reitit.ring.middleware.parameters :as par]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [web.views.views :as views]))

(println "in ns:" (str *ns*))


(def middleware-db
  {:name ::db
   :compile (fn [{:keys [db]} _]
              (fn [handler]
                (fn [req]
                  (handler (assoc req :db db)))))})

(defn app [db]
  (r/ring-handler
   (r/router
    [["/"      views/home]
     ["/login" views/login]]

    {:data {:db db
            :middleware [par/parameters-middleware
                         wrap-keyword-params
                         middleware-db]}})

   (r/routes
    (r/redirect-trailing-slash-handler)

    (r/create-resource-handler
     {:path "/"})

    (r/create-default-handler
     {:not-found (constantly {:status 404 :body "Not found"})
      :method-not-allowed (constantly {:status 405 :body "Method not allowed"})
      :not-acceptable (constantly {:status 406 :body "Not acceptable"})}))))
