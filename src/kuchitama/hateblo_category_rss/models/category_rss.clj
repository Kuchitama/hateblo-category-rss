(ns kuchitama.hateblo-category-rss.models.category-rss
	(:require [net.cgrand.enlive-html :as enlive]
              [clj-http.client :as client]))

(def ^:dynamic url "http://kuchitama.hateblo.jp/feed")

(defn parse-rss
  [xml]
  (enlive/xml-resource
    (java.io.StringReader. xml)))

(defn get-target-category
  [mapped-xml category]
          (for [entry (enlive/select mapped-xml [:entry])
                term (enlive/select entry [[:category (enlive/attr-contains :term category)]])
                ]
                      entry))

(defn remove-entry
  [mapped-xml]
    (enlive/at mapped-xml [:entry] nil))

(defn create-rss-map
  [mapped-xml category]
  (enlive/transform (remove-entry mapped-xml)
            [:feed]
             (enlive/append (get-target-category mapped-xml category))))

(defn get-rss
  []
  (:body (client/get url)))

(defn category-rss
  [category]
  (create-rss-map 
    (parse-rss (get-rss)) category))
    
