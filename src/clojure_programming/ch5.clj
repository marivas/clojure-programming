(ns clojure-programming.ch5)

(defn force-retries [x]
  (dotimes [t 5]
    (.start (Thread. #(dosync
                       (Thread/sleep (rand-int 10))
                       (println "Thread" t)
                       (alter x inc))))))

(def x (ref 0))
