(ns clojure-programming.ch2)

(defn multiplier
  [x]
  (fn [n] (* n x)))

(def m2 (multiplier 2))

(m2 50) ; => 100

((multiplier 0.3) 100) ; => 30.0


