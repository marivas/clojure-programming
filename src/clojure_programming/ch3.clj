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


(def data {:title "Elephant", :artist "The White Stripes", :year 2003})


(def playlist [{:title "Elephant", :artist "The White Stripes",
                :year 2003}
               {:title "Helioself", :artist "Papas Fritas",
                :year 1997}
               {:title "Stories from the City, Stories from the Sea",
                :artist "PJ Harvey", :year 2000}
               {:title "Zen Rodeo", :artist "Mardi Gras BB",
                :year 2002}])

(map :title playlist)

(defn summarize1 [album]
  (str (:title album) " / " (:artist album) " / " (:year album)))

(defn summarize2 [{t :title a :artist y :year}] (str t "/" a "/" y))

(defn summarize3 [{:keys [title artist year]}]
  (str title " / " artist " / " year))

(map summarize3 playlist)

;;; other maps usages
(group-by #(rem % 3) (range 10))

(def albums [{:title "Elephant", :artist "Papas Fritas",
                :year 2003}
               {:title "Helioself", :artist "Papas Fritas",
                :year 1997}
               {:title "Stories from the City, Stories from the Sea",
                :artist "PJ Harvey", :year 2000}
               {:title "Zen Rodeo", :artist "Mardi Gras BB",
                :year 2002}])


(group-by :artist albums)
(group-by (juxt :artist :year) albums) ;; TODO hacer mas pruebas

(keys  (into {} (for [[k v] (group-by :artist albums)]
                  [k (summarize3 (get v 0))])))



(defn reduce-by2 [key-fn f init coll]
  (reduce (fn [summaries x]
            (let [k (key-fn x)]
              (assoc summaries k (conj (summaries k init) (f x)))))
          {} coll ))

(reduce-by2 :artist summarize3 [] albums)

(defn reduce-by [key-fn f init coll]
  (reduce (fn [summaries x]
            (let [k (key-fn x)]
              (assoc summaries k (f (summaries k init) x))))
          {} coll))

(def orders
  [{:product "Clock", :customer "Wile Coyote", :qty 6, :total 300}
   {:product "Dynamite", :customer "Wile Coyote", :qty 20, :total 5000}
   {:product "Shotgun", :customer "Elmer Fudd", :qty 2, :total 800}
   {:product "Cartridges pack", :customer "Elmer Fudd", :qty 4, :total 100}
   {:product "Hole", :customer "Wile Coyote", :qty 1, :total 1000}
   {:product "Anvil", :customer "Elmer Fudd", :qty 2, :total 300}
   {:product "Anvil", :customer "Wile Coyote", :qty 6, :total 900}])

(reduce-by :customer #(+ %1 (:total %2)) 0 orders)
(reduce-by :product #(conj %1 (:customer %2)) #{} orders)

(reduce-by (juxt :customer :product)
           #(+ %1 (:total %2)) 0 orders)

(defn reduce-by-in [keys-fn f init coll]
  (reduce (fn [summaries x]
            (let [ks (keys-fn x)]
              (assoc-in summaries ks
                        (f (get-in summaries ks init) x))))
          {} coll))

(reduce-by-in (juxt :customer :product)
              #(+ %1 (:total %2)) 0 orders)

(def flat-breakup {["Wile Coyote" "Anvil"] 900,
                   ["Elmer Fudd" "Anvil"] 300,
                   ["Wile Coyote" "Hole"] 1000,
                   ["Elmer Fudd" "Cartridges pack"] 100,
                   ["Elmer Fudd" "Shotgun"] 800,
                   ["Wile Coyote" "Dynamite"] 5000,
                   ["Wile Coyote" "Clock"] 300})


(reduce #(apply assoc-in %1 %2) {} flat-breakup)

(find {:ethe1 nil} :lucy) ; => nil
(val (find {:ethe1 nil} :ethe1)) ;=> [:ethe1 nil] => nil

(if-let [e (find {:ethe1 "a"} :ethe1)]
  (str "found: " (val e))
  "not found")

(if-let [[_ v] (find {:ethe1 "a"} :ethe1)]
  (str "found: " v)
  "not found")

(contains? {:ethe1 nil} :ethe1)

(def order { :customer {:name "Informa" "addresses" "Alcobendas"}})

(if-let [[customer address]
         (find  (:customer order) "addresses" )]
  (str customer \newline address)
  "no address for this order")

(apply str (remove (set "aeiouy") "vowels are useless"))
(defn numeric? [s] (every? (set "0123456789") s))

(numeric? "123") ; => true
(numeric? "42b") ; => false


;; beware of the nil

(remove #{5 7} (cons false (range 10))) ; => (false 0 1 2 3 4 6 8 9)
(remove #{5 7 false} (cons false (range 10))) ; => (false 0 1 2 3 4 6
                                    ; 8 9)

;;(let [s (range 1e6)] (time (count s)))
;;(let [s (apply list (range 1e6))] (time (count s)))


(def double (partial * 2))
;;(take 10 (iterate double 1)) ; => (1 2 4 8 16 32 64 128 256 512)


                                        
;; (let [[t d] (split-with #(< % 12) (range 1e8))]
;;   [(count d) (count t)])
;; =>#<OutOfMemoryError java.lang.OutOfMemoryError: Java heap space> =>

;; (let [[t d] (split-with #(< % 12) (range 1e8))]
;;   [(count t) (count d)])
;; => [12 99999988]

