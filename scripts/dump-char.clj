(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])

;;; 文字種の一覧をdumpする

(defn doit [args]
  (let [a (atom #{})]
    (loop []
      (when-let [line (read-line)]
        (doseq [c (seq line)]
          (when-not (get @a c)
            (swap! a conj c)))
        (recur)))
    (doseq [c (sort @a)]
      (println c))))

(doit *command-line-args*)
