(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])


(defn usage [args]
  (binding [*out* *err*]
    (println "usage : lein exec" (first args) "[input-file] > output-file")))

(defn inc* [n]
  (inc (or n 0)))

;; TODO: support not only bigram but also n-gram
(defn doit [args]
  (if-let [input-file (second args)]
    (let [lines (line-seq (io/reader input-file))
          length-table (atom {})
          bigram-table (atom {})]
      (doseq [line lines]
        (when-not (or
                    (= "" line)
                    (= \# (first line)))
          ;; update length-table
          (swap! length-table update (count line) inc*)
          ;; update bigram-table
          (loop [prev \^
                 left (seq (str line \$))]
            (let [current (first left)]
              (swap! bigram-table update (str prev current) inc*)
              (when-not (= current \$)
                (recur current (rest left)))))))
      ;; output length-table
      ;; (doseq [len (sort (keys @length-table))]
      ;;   (println len (get @length-table len)))
      ;; (newline)
      ;; output bigram-table
      (doseq [k (sort (keys @bigram-table))]
        (println k (get @bigram-table k))))
    (usage args)))

(doit *command-line-args*)
