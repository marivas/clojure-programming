(ns clojure-programming.ch5-macros)

(require '(clojure [string :as str]
                   [walk :as walk]))



(macroexpand-1 '(if-not a b))
(walk/macroexpand-all ''(when x a))

(defmacro foreach [[sym coll] & body]
  `(loop [coll# ~coll]
     (when-let [[~sym & xs#] (seq coll#)]
       ~@body
       (recur xs#))))

(foreach [x [1 2 3]]
         (println x))

(keyword "hola")

(defn- mangle*
  [code]
  (butlast (reverse code)))

(defmacro mangle
  [& body]
  (mangle* body))

(mangle (throw (Exception. "DIE!")) 20 30 +) ;=> 50

(defmacro reverse-it [form]
  (walk/postwalk #(if (symbol? %)
                    (symbol (str/reverse (name %)))
                    %)
                 form))

(reverse-it (qesod [gra (egnar 5)]
                   (nltnirp (cni gra))))
;;(reverse-it '(uno dos))

;;(defn oops [arg] (frobnicate arg))


(defmacro hello [name]
  (list 'println name))

(hello "Brian")

(defmacro while [test & body]
  (list 'loop []
        (concat (list 'when test) body)
        (list 'recur)))

;;(while (= 2 2) 4)

(defmacro while
  [test & body]
  `(loop []
     (when ~test
       ~@body
       (recur))))

;;(while (= 2 2) 4)

(let [defs '((def x 123)
             (def y 456))]
  (concat (list 'do) defs))

(let [defs '((def x 123)
             (def y 456))]
  `(do ~@defs))

(defmacro foo
  [& body]
  `(do-something ~@body))

(foo (doseq [x (range 5)]
       (println x))
     :done)

