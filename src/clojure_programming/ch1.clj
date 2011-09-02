(ns clojure-programming.ch1)

(def v [42 "foo" 99.2 [5 12]])

;; destructuring vector
(let [[x _ _ [y z]] v]
  (+ x y z))

(let [[x & rest] v]
  rest)

(let [[x _ z :as original-vector] v]
  (conj original-vector (+ x z)))

;; destructuring maps
(def m {:a 5 :b 6
        :c [7 8 9]
        :d {:e 10 :f 11}
        "foo" 88
        42 false})

(let [{a :a b :b} m]
  (+ a b)) ; => 11

(let [{f "foo"} m]
  (+ f 12)) ; not only keywords valid for lookup

(let [{x 3 y 8} [12 0 0 -18 44 6 0 0 1]]
  (+ x y)) ; => -17 ; using indices vector, same whith string and
                                        ; arrays


(let [{{e :e} :d} m]
  (* 2 e)) ; => 20

()
(let [{[x _ y] :c} m]
  (+ x y)) ; mix destructuring map and vector

(def map-in-vector ["Chas" {:birthday (java.util.Date. 80 2 6)}])

(let [[name {bd :birthday}] map-in-vector]
  (str name "was born on " bd))

;;; :as
(let [{r1 :x r2 :y :as randoms}
      (zipmap [:x :y :z] (repeatedly (partial rand-int 10)))]
  (assoc randoms :sum (+ r1 r2)))

;;; default values
(let [{k :unknown x :a :or {k 50}} m]
  (+ k x))

;;; best that ..
(let [{k :unknown x :a} m
      k (or k 50)]
  (+ k x))

(def chas {:name "Chas" :age 31 :location "Massachusetts"})

;;; :keys , same for :strs and :syms
(let [{:keys [name age location]} chas]
  (format "%s is %s years old and lives in %s." name age location))


(def user-info ["rober8990" 2011 :name "Bob" :city "Boston"])

;;; Manual aproach
(let [[username account-year & extra-info] user-info
      {:keys [name city]} (apply hash-map extra-info)]
  (format "%s is in %s" name city))

;;; now best ...

(let [[username account-year & {:keys [name city]}] user-info]
  (format "%s is in %s" name city))

