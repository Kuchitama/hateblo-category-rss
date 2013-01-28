(ns kuchitama.hateblo-category-rss.views.rss
  (:require [noir.content.getting-started]
            [noir.response :as response]
            [clj-http.client :as client]
            [net.cgrand.enlive-html :as enlive]
            [kuchitama.hateblo-category-rss.models.category-rss :as rss])
  (:use [noir.core :only [defpage]]))

(defpage [get "/rss/:category"] {:keys [category]}
         (response/xml (apply str (enlive/emit*
                         (rss/category-rss (str "" category))))))
