(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])


(defn usage [args]
  (binding [*out* *err*]
    (println "usage : cat input-file | lein exec" (first args) "[mark] > output-file")))

(defn doit [args]
  (if-let [mark (second args)]
    (loop []
      (when-let [line (read-line)]
        (cond
          (= "" line) (newline)
          (re-find #"^#" line) nil
          :else (when-let [[m stripped] (re-find #"^. .*" line)]
                  (when (= mark m)
                    (println stripped))))
        (recur)))
    (usage args)))

(doit *command-line-args*)
