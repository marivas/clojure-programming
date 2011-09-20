(ns clojure-programming.ch3)

(defn my-into [a b]
  (if (empty? b)
    a
    (recur (conj a (first b)) (rest b))))

(defn my-into2 [a b]
  (reduce conj a b)) ;; magic!!!!!


(= (seq [1 2 3]) (rseq [3 2 1]))


(defn euclidian-division [x y]
  [(quot x y) (rem x y)])

(euclidian-division 42 8) ; => [5 2]

(let [[q r] (euclidian-division 53 7)]
  (str "53/7=" q "*7+" r))


