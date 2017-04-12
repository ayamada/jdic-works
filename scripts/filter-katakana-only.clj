(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])


(defn inc* [n]
  (inc (or n 0)))

(def katakana?
  #{
    \ァ \ア \ィ \イ \ゥ \ウ \ェ \エ \ォ \オ \カ \ガ \キ \ギ \ク
    \グ \ケ \ゲ \コ \ゴ \サ \ザ \シ \ジ \ス \ズ \セ \ゼ \ソ \ゾ \タ
    \ダ \チ \ヂ \ッ \ツ \ヅ \テ \デ \ト \ド \ナ \ニ \ヌ \ネ \ノ \ハ
    \バ \パ \ヒ \ビ \ピ \フ \ブ \プ \ヘ \ベ \ペ \ホ \ボ \ポ \マ \ミ
    \ム \メ \モ \ャ \ヤ \ュ \ユ \ョ \ヨ \ラ \リ \ル \レ \ロ \ヮ \ワ
    \ヰ \ヱ \ヲ \ン \ヴ \ヵ \ヶ \ヷ \ヸ \ヹ \ヺ
    \・ \ー
    })


(defn half-width? [c]
  (< (long c) 256))

(defn ok? [line]
  (and
    (not= "" line)
    (every? katakana? (seq line))))

(defn doit [args]
  (loop []
    (when-let [line (read-line)]
      (when (ok? line)
        (println line))
      (recur))))

(doit *command-line-args*)
