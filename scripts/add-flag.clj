(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])



(defn doit [args]
  (let [mode-char (if (second args) "o" "x")]
    (loop []
      (when-let [line (read-line)]
        (cond
          (= "" line) (newline)
          (re-find #"^#" line) nil
          :else (println mode-char line))
        (recur)))))

(doit *command-line-args*)
