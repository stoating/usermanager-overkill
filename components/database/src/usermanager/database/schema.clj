(ns usermanager.database.schema
  (:require [malli.core :as m]))

(def Department :string)

(def User
  [:map
   [:first_name :string]
   [:last_name :string]
   [:email #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$"]
   [:department_id :int]])

(comment
  (m/validate Department "Engineering")
  (m/validate Department 1)
  )