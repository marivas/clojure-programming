(ns clojure-programming.gol)


(defn empty-board
  "Creates a rectangular empty board of the specified width and height."
  [w h]
  (vec (repeat w (vec (repeat h nil)))))

(defn populate-board []
  (-> (empty-board 5 5)
      (assoc-in [1 2] :on)
      (assoc-in [2 2] :on)
      (assoc-in [3 2] :on)))

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


(take 3 (iterate step ( populate-board)))
;;([
;; [nil nil nil nil nil]
;; [nil nil :on nil nil]
;; [nil nil :on :on nil]
;; [nil nil nil nil nil]
;; [nil nil nil nil nil]]
;; [
;; [nil nil nil nil nil]
;; [nil nil :on :on nil]
;; [nil nil :on :on nil]
;; [nil nil nil nil nil]
;; [nil nil nil nil nil]]
;; [
;; [nil nil nil nil nil]
;; [nil nil :on :on nil]
;; [nil nil :on :on nil]
;; [nil nil nil nil nil]
;; [nil nil nil nil nil]
;; ])

