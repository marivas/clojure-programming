(ns clojure-programming.ch1-funcs)

(def strange-adder (fn adder-self-reference
                     ([x] (adder-self-reference x 1))
                     ([x y] (+ x y))))

(strange-adder 10)
(strange-adder 10 50)

;;; variadic functions
(defn concat-rest
  [x & rest]
  (apply str (butlast rest)))

(concat-rest 0 1 2 3 4) ; => "123"

(defn make-user
  [& [user-id]]
  {:user-id (or user-id
                (str (java.util.UUID/randomUUID)))})

(make-user "Bobby")

;;; keyword arguments

(defn make-user
  [username & {:keys [email join-date]
               :or {join-date (java.util.Date.)}}]
  {:username username
   :join-date join-date
   :email email
   ;; 2.592E9 -> one month in ms
   :exp-date (java.util.Date. (long (+ 2.592E9 (.getTime join-date))))})

(make-user "Bobby")
(make-user "Bobby" :join-date (java.util.Date. 2011 0 1) :email "bobby@gmail.com")


;; a collection as key in a map destructuring
(defn foo
  [& {k ["m" 9]}]
  (inc k))

(foo ["m" 9] 19) ; => 20

;;; Pre- and post-conditions
(defn halve
  [x]
  {:pre [(pos? x) (even? x)]}
  (/ x 2))

(defn pos-max
  [& args]
  {:pre [(seq args)]
   :post [(pos? %)]}
  (apply max args))

(pos-max) ; => java.lang.AssertionError: Asserte failed: (seq args)
(pos-max 5 10 8) ;=> 10
(pos-max -40 0) ;=> java.lang.AssertionError: Asserte failed: (pos? %)






