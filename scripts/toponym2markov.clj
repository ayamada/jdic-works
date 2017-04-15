(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])


(defn inc* [n]
  (inc (or n 0)))

(defn doit [args]
  (let [ngram-max (if-let [nstr (second args)]
                    (Integer. nstr)
                    2)
        ngram-max (max 2 ngram-max)
        result (atom {})]
    ;; tally
    (loop []
      (when-let [line (read-line)]
        (when-not (or
                    (= "" line)
                    (= \# (first line)))
          (let [line (str "^" line "$")
                line-len (count line)]
            (dotimes [ngram-n (inc line-len)]
              (when (<= 2 ngram-n ngram-max)
                (dotimes [i line-len]
                  (let [end (+ i ngram-n)]
                    (when (<= end line-len)
                      (swap! result update (subs line i end) inc*))))))
            (recur)))))
    ;; output to sorted result
    (doseq [k (sort (keys @result))]
      (println k (get @result k)))))

(doit *command-line-args*)
