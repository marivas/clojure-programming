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

(comment 
  (pos-max) ; => java.lang.AssertionError: Asserte failed: (seq args)
  (pos-max 5 10 8) ;=> 10
  (pos-max -40 0) ;=> java.lang.AssertionError: Asserte failed: (pos? %)
)

;;; Using conditions effectivelly.
;;; Check entities

(require 'clojure.set)

(defn earnings-entity?
  [x]
  (clojure.set/subset? #{:name :date :earnings} (set (keys x))))

(defn save-earnings-entity
  [x]
  {:pre [(earnings-entity? x)]}
  ;; persist in bd
  true)

(save-earnings-entity {:name "Lori Gilles"
                       :date "20110803"
                       :earnings 56810}) ; => true

(comment  (save-earnings-entity {:name "Lori Gilles"
                         :date "20110803"})) ; => false  Assert Failed
                                        ; (earnings-entity? x)

;; Equivalentes, las dos dan 1 como resultado
((fn [x & rest] (- x (apply + rest))) 5 3 1)
(#(- % (apply + %&)) 5 3 1)

(if "hi" \t) ; => \t
(if 42 \t) ; => \t
(if nil "unevaluated" \f) ; => \f
(if false "unevaluated" \f) ; => \f
(if (not true) \t) ; => nil   if not else expression is provided, the
                                        ; result is nil


;;; looping: loop and recur
(loop [x 5]
  (if (neg? x)
    x
    (recur (dec x))))

(defn countdown
  [x]
  (if (zero? x)
    :blastoff!
    (do (println x)
        (recur (dec x)))))

;;; vars
(def x 5)
(var x) ; => #'clojure-programming.ch1-funcs/x
#'clojure-programming.ch1-funcs/x ;=> same that (var x)


;;; Java interop: . and new

