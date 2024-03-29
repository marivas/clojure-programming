(ns clojure-programming.ch2)

(defn multiplier
  [x]
  (fn [n] (* n x)))

(def m2 (multiplier 2))

(m2 50) ; => 100

((multiplier 0.3) 100) ; => 30.0


;;(doc rand-int)

(map inc [1 2 3 4])

(map vector [1 2 3 4] [ "a" "b" "c" "d" ])

(reduce + 50 [1 2 3 4])

(reduce
 (fn [m v]
   (assoc m v (str v)))
 {}
 [1 2 3 4])

(require '[clojure.zip :as z])
(def v [[3 4 5] 4 [2 5 6]])


(-> v z/vector-zip z/down z/node)
(-> v z/vector-zip z/node)

(defn blank? [s]
  (every? #(Character/isWhitespace %) s))


(defn fib
  [current next n]
  (if (zero? n)
    current
    (recur next (+ current next) (dec n))))

(defn tail-fibo [n]
  (letfn [(fib
            [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn sum
  [& numbers]
  (reduce + numbers))

(sum 12 12 12 12)
(apply sum '(12 12 12 12))
