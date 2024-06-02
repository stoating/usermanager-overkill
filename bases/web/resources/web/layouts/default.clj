(ns web.layouts.default
  (:require [web.components.navbar :as navbar]))

(println "in ns:" (str *ns*))


(def html
  [:html {:lang "en-US"}])


(def head
  (let [title "my_title"
        description "my_description"]
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
  [body]
    (conj
     html
     head
     [:body {:class ["absolute" "w-full" "min-h-full" "flex" "flex-col"]}
      [:.flex-grow]
      [:div {:class ["p-3" "mx-auto" "max-w-screen-sm" "w-full"]}
       navbar/navbar
       body]
      [:.flex-grow]
      [:.flex-grow]]))


(defn wrap-html-body
  [& body]
  (apply body-shell body))
