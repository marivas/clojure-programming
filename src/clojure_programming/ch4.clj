(ns clojure-programming.ch4)

(defn foo [] (println "Ruunning..."))

(.start (Thread. foo))


;; (defn build-queue []
;;   (let [q (java.util.concurrent.LinkedBlockingQueue.)]
;;     (.start (Thread.
;;              #(while true
;;                 (prn "Item consumed:" (.take q)))))
;;     q))

;; (def q (build-queue))

;; (.add q "foo")


