(ns web.views.views
  (:require [rum.core :as rum]
            [ring.util.response :as rr]
            [web.views.home :as home]
            [web.views.login :as login]))

(defn login [_]
  (rr/response (str login/layout)))