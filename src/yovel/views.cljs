(ns yovel.views
  (:require
   [re-frame.core :as re-frame]
   [re-com.core :as re-com :refer [at]]
   [breaking-point.core :as bp]
   [yovel.events :as events]
   [yovel.routes :as routes]
   [yovel.subs :as subs]
   ;;[c2.scale :as scale]
   )
  ;;(:use [c2.core :only [unify]])
  )

;; home

(defn display-re-pressed-example []
  (let [re-pressed-example (re-frame/subscribe [::subs/re-pressed-example])]
    [:div

     [:p
      [:span "Re-pressed is listening for keydown events. A message will be displayed when you type "]
      [:strong [:code "hello"]]
      [:span ". So go ahead, try it out!"]]

     (when-let [rpe @re-pressed-example]
       [re-com/alert-box
        :src        (at)
        :alert-type :info
        :body       rpe])]))

(defn home-title []
  (let [name (re-frame/subscribe [::subs/name])]
    [re-com/title
     :src   (at)
     :label (str "Hello from " @name ". This is the Home Page." )
     :level :level1]))

(defn link-to-about-page []
  [re-com/hyperlink
   :src      (at)
   :label    "go to About Page"
   :on-click #(re-frame/dispatch [::events/navigate :about])])

(defn home-panel []
  [re-com/v-box
   :src      (at)
   :gap      "1em"
   :children [[home-title]
              [link-to-about-page]
              [display-re-pressed-example]
              [:div
               [:h3 (str "screen-width: " @(re-frame/subscribe [::bp/screen-width]))]
               [:h3 (str "screen: " @(re-frame/subscribe [::bp/screen]))]]]])


(defmethod routes/panels :home-panel [] [home-panel])

;; chart

#_(defn bar-chart []
  (let [width 500, bar-height 20
        data {"A" 1, "B" 2, "C" 4, "D" 3}
        s (scale/linear :domain [0 (apply max (vals data))]
                        :range [0 width])]
    [:div#bars
     (unify data (fn [[label val]]
                   [:div {:style {:height bar-height
                                  :width (s val)
                                  :background-color "gray"}}
                    [:span {:style {:color "white"}} label]]))]))

;; about

(defn about-title []
  [re-com/title
   :src   (at)
   :label "This is the About Page."
   :level :level1])

(defn link-to-home-page []
  [re-com/hyperlink
   :src      (at)
   :label    "go to Home Page"
   :on-click #(re-frame/dispatch [::events/navigate :home])])

(defn about-panel []
  [re-com/v-box
   :src      (at)
   :gap      "1em"
   :children [[about-title]
              [link-to-home-page]]])

(defmethod routes/panels :about-panel [] [about-panel])

;; main

(defn main-panel []
  (let [active-panel (re-frame/subscribe [::subs/active-panel])]
    [re-com/v-box
     :src      (at)
     :height   "100%"
     :children [(routes/panels @active-panel)]]))
