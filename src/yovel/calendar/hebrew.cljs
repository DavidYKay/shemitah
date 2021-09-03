(ns yovel.calendar.hebrew
  (:require ["@hebcal/core" :as hebcal :refer [Event
                                               HDate
                                               HebrewCalendar
                                               Location]]))

(def events
  [

   ["Great Crash" [1929 10 24]]
   ["Sept 11" [2001 9 11]]
   ["Lehman Bankruptcy" [2008 9 14 ]]
   ["Oil Crisis" [1973 10 8]]
   ["Black Monday" [1987 10 19]]

   ["Closing of the gold window" [1971 8 15]]

   ["US Enters WW1" [1917 4 6]]
   ["End of WW2" [1945 9 2]]
   ["The Great Crash" [1929 10 24]]

   ])

(def hope-to-prove
  [
   ["End of WW1" [1918 11 11]]

   ["Establishment of Federal Reserve" [1913 12 23]]
   ])

(def options
  #js
  {:year 5782
   :isHebrewYear true
   :candlelighting false
   :location (.lookup Location "San Francisco")
   :sedrot true
   :omer true})

(def events (.calendar HebrewCalendar options))

(defn stringify [hd]
  (str (-> hd
           .greg
           .toLocaleDateString)
    " (AM " (.getFullYear hd) ")"))

(defn pretty-print [hd]
  (println
   (-> hd
       .greg
       .toLocaleDateString )
   (.toString hd)))

(defn print-events [events]
  (doseq [ev events]
    (let [hd (.getDate ev)
          date (.greg hd)]
      (println (.toLocaleDateString date) (.render ev) (.toString hd)))))

(defn is-rosh? [ev]
  (let [hd (.getDate ev)]
    (and
     (= 1 (.getTishreiMonth hd))
     (= 1 (.getDate hd)))))

(defn event-to-date [ev]
  (let [hd (.getDate ev)
        date (.greg hd)
        tm (.getTishreiMonth hd)
        td (.getDate hd)
        ty (.getFullYear hd)]
    [date [ty tm td]]))

(defn when-rosh []
  (->> events
       (filter is-rosh?)
       first
       event-to-date))

(defn shemitahs []
  (loop [cur (HDate. 1 7 5782)
         dates-rem 16
         accum []]
    (if (= dates-rem 0)
      accum
      (recur (.subtract cur 7 "y")
             (dec dates-rem)
             (conj accum [cur
                          (.add cur 1 "y")])))))

(defn print-shemitahs []
  (->> (shemitahs)
       reverse
       (map (fn [[a b]]
              (println
               (stringify a)
               " -> "
               (stringify b))))))
