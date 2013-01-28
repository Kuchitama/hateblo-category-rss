(ns kuchitama.hateblo-category-rss.views.welcome
  (:require [kuchitama.hateblo-category-rss.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to hateblo-category-rss"]))
