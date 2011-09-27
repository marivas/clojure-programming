(ns clojure-programming.gol)


(defn empty-board
  "Creates a rectangular empty board of the specified width and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(defn populate-board []
  (-> (empty-board 5 5)
      (assoc-in [1 2] :on)
      (assoc-in [2 2] :on)
      (assoc-in [2 3] :on)))

(defn neighbours [[x y]]
  (for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
    [(+ dx x) (+ dy y)]))

(defn count-neighbours [board loc]
  (count (filter #(get-in board %) (neighbours loc))))

(defn step
  "Yields the next state of the board"
  [board]
  (let [w (count board)
        h (count (first board))]
    (loop [new-board board x 0 y 0]
      (cond
       (>= x w) new-board
       (>= y h) (recur new-board (inc x) 0)
       :else
       (let [new-liveness
             (case (count-neighbours board [x y])
               2 (get-in new-board [x y])
               3 :on
               nil)]
         (recur (assoc-in new-board [x y] new-liveness) x (inc y)))))))


;;; Step2 - with reduce
(defn step2
  "Yields the next state of the board"
  [board]
  (let [w (count board)
        h (count (first board))]
    (reduce
     (fn [board x]
       (reduce
        (fn [board y]
          (let [new-liveness
                (case (count-neighbours board [x y])
                  2 (get-in board [x y])
                  3 :on
                  nil)]
            (assoc-in board [x y] new-liveness)))
        board (range h)))
     board (range w))))

(defn step3
  "Yields the next state of the board"
  [board]
  (let [w (count board)
        h (count (first board))]
    (reduce
     (fn [board [x y]]
       (let [new-liveness
             (case (count-neighbours board [x y])
               2 (get-in board [x y])
               3 :on
               nil)]
         (assoc-in board [x y] new-liveness)))
     board (for [x (range h) y (range w)] [x y]))))


(take 3 (iterate step ( populate-board)))


(partition 3 2 '(0 0 0) (range -1 5))

(partition 3 1 (concat [nil] (range 5) [nil]))

(defn window
  "Returns a lazy sequence of 3-item windows centered around each item in coll"
  [coll]
  (partition 3 1 (concat [nil] coll [nil])))

(defn cell-block
  "Creates a sequences of 3x3 windows from  a triple of 3 sequences"
  [[left mid right]]
  (window (map vector
               (or left (repeat nil))
               mid
               (or right (repeat nil)))))

(map cell-block (window (populate-board)))
(first (window (populate-board)))
;;=> (nil [nil nil nil nil nil] [nil nil :on nil nil])

(cell-block (first (window (populate-board))))
;;=> ((nil [nil nil nil] [nil nil nil])
;;  ([nil nil nil] [nil nil nil] [nil nil :on])
;;  ([nil nil nil] [nil nil :on] [nil nil nil])
;;  ([nil nil :on] [nil nil nil] [nil nil nil])
;;  ([nil nil nil] [nil nil nil] nil))

(window (map vector (repeat nil) [nil nil nil nil nil] [nil nil :on nil nil]))

(defn window2
  "Returns a lazy sequence of 3-item windows centered around each item of coll, padded as necessary with pad or nil."
  ([coll] (window2 nil coll))
  ([pad coll]
     (partition 3 1 (concat [pad] coll [pad]))))

(defn cell-block2
  "Creates a sequences of 3x3 windows from a triple of 3 sequences."
  [[left mid right]]
  (window2 (map vector left mid right)))

(defn liveness2
  "Returns the liveness (nil or :on) of the center cell for the next step."
  [block]
  (let [[_ [_ center _] _] block]
    (case (- (count (filter #{:on} (apply concat block)))
             (if (= :on center) 1 0))
      2 center
      3 :on
      nil)))

(defn- step-row
  "Yields the next state of the center row."
  [rows-triple]
  (vec (map liveness2 (cell-block2 rows-triple))))

(defn step2
  "Yields the next state of the board"
  [board]
  (vec (map step-row (window2 (repeat nil) board))))

(take 3 (iterate step2 (populate-board)))

(defn step3
  "Yields the next state of the world"
  [cells]
  (set (for [[loc n] (frequencies (mapcat neighbours cells ))
             :when (or (= n 3) (and (= n 2) (cells loc)))]
         loc)))

(def glider #{[0 1] [1 2] [2 0] [2 1] [2 2]})
(step3 glider)

;;; with high order function
(defn stepper
  "Returns a step function for Life-like cell automata. neighbours takes a location and return a sequential collection of locations. survive? and birth? are predicates on the number of living neighbours."
  [neighbours birth? survive?]
  (fn [cells]
    (set (for [[loc n] (frequencies (mapcat neighbours cells))
               :when (if (cells loc) (survive? n) (birth? n))]
           loc))))

(def step4 (stepper neighbours #{3} {2 3}))

(step4 glider)

(defn hex-neighbours [[x y]]
  (for [dx [-1 0 1] dy (if (zero? dx) [-2 2] [-1 1])]
    [(+ dx x) (+ dy y)]))

(def hex-step (stepper hex-neighbours #{2} {3 4}))

(hex-step #{[0 0] [1 1] [1 3] [0 4]})

