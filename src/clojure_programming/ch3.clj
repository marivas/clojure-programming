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



(defn reduce-by [key-fn f init coll]
  (reduce (fn [summaries x]
            (let [k (key-fn x)]
              (assoc summaries k (conj (summaries k init) (f x)))))
          {} coll ))

(reduce-by :artist summarize3 [] albums)

