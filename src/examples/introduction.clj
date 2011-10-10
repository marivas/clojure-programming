(ns examples.introduction)

(def visitors (ref #{}))

(defn hello
  "Writes hello message to *out*. Calls yout by username.
   Knows if you haven been here before."
  [username]
  (dosync
   (let [past-visitor (@visitors username)]
     (if past-visitor
       (str "Welcome back, " username)
       (do
         (alter visitors conj username)
         (str "Hello, " username))))))

(defn greeting
  "Return a greeting of the form 'Hello, username.'
   Default username is 'world'."
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))



(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2
           "went out with" (count chaperones) "chaperones"))

(date "Rome" "Juliet" "Friar Lawrence" "Nurse")
;; Rome and Juliet went out with 2 chaperones

(defn indexable-word? [word]
  (> (count word) 2))

(require '[clojure.string :as str])

(filter indexable-word? (str/split "A fine day it is" #"\W+"))
(filter (fn [w] (> (count w) 2)) (str/split "A fine day it is" #"\W+"))
(filter #(> (count %) 2) (str/split "A fine day it is" #"\W+"))
;; ("fine" "day")

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(indexable-words "A fine day it is")
;; ("fine" "day")










