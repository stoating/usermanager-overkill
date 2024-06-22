(ns web.layouts.default
  (:require [portal.api :as p]
            [web.components.changes-counter :as changes-counter]
            [web.components.message-toggle :as message-toggle]
            [web.components.navbar :as navbar]))


(println "in ns:" (str *ns*))
(add-tap #'p/submit)


(def html
  [:html {:lang "en-US"}])


(def head
  (let [title "stoating"
        description "just stoating around..."]
    [:head
     [:title title]
     [:meta {:name "description" :content description}]
     [:meta {:property "og:title"
             :content title}]
     [:meta {:property "og:description"
             :content description}]
     [:meta {:property "og:image"
             :content "https://clojure.org/images/clojure-logo-120b.png"}]
     [:meta {:name "twitter:card"
             :content "summary_large_image"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:meta {:charset "utf-8"}]
     [:link {:rel "icon"
             :type "image/png"
             :sizes "16x16"
             :href "/img/favicon-16x16.png"}]
     [:link {:rel "stylesheet" :href "/css/tailwind_output.css"}]
     [:script {:src "/js/htmx.min.js"}]]))


(defn body-shell
  [resp body]
  (conj
   html
   head
   [:body {:class ["bg-gray-300"]}
    [:header {:class ["relative w-screen"]}
     navbar/component]
    [:.flex-grow]
    [:div {:class ["mx-auto" "max-w-screen-sm" "w-full" "py-3"]}
     [:. {:class ["md:px-0 px-1 py-3"]}
      body]
     [:. {:class ["md:px-0 px-1 py-3"]}
      (changes-counter/component resp)]
     [:. {:class ["md:px-0 px-1 py-3"]}
      message-toggle/component]]
    [:.flex-grow]
    [:.flex-grow]]))


(defn wrap-html-body
  [resp & body]
  (apply body-shell resp body))
