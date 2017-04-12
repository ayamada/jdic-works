(require '[leiningen.exec :refer [deps]])
;(deps '[[foo/bar-core "1.2.3"]])
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])


(defn inc* [n]
  (inc (or n 0)))

(def exclude-table
  #{
    \ァ \ア \ィ \イ \ゥ \ウ \ェ \エ \ォ \オ \カ \ガ \キ \ギ \ク
    \グ \ケ \ゲ \コ \ゴ \サ \ザ \シ \ジ \ス \ズ \セ \ゼ \ソ \ゾ \タ
    \ダ \チ \ヂ \ッ \ツ \ヅ \テ \デ \ト \ド \ナ \ニ \ヌ \ネ \ノ \ハ
    \バ \パ \ヒ \ビ \ピ \フ \ブ \プ \ヘ \ベ \ペ \ホ \ボ \ポ \マ \ミ
    \ム \メ \モ \ャ \ヤ \ュ \ユ \ョ \ヨ \ラ \リ \ル \レ \ロ \ヮ \ワ
    \ヰ \ヱ \ヲ \ン \ヴ \ヵ \ヶ \ヷ \ヸ \ヹ \ヺ
    \— \− \　 \、 \。
    ;\々 \〆
    \〜
    \ぁ \あ \ぃ \い \ぅ \う \ぇ \え \ぉ \お \か \が \き \ぎ \く
    \ぐ \け \げ \こ \ご \さ \ざ \し \じ \す \ず \せ \ぜ \そ \ぞ \た
    \だ \ち \ぢ \っ \つ \づ \て \で \と \ど \な \に \ぬ \ね \の \は
    \ば \ぱ \ひ \び \ぴ \ふ \ぶ \ぷ \へ \べ \ぺ \ほ \ぼ \ぽ \ま \み
    \む \め \も \ゃ \や \ゅ \ゆ \ょ \よ \ら \り \る \れ \ろ \ゎ \わ
    \ゐ \ゑ \を \ん
    \・ \ー \＆ \（ \） \／
    \０ \１ \２ \３ \４ \５ \６ \７ \８ \９
    ;\Ａ \Ｂ \Ｃ \Ｄ \Ｅ \Ｆ \Ｇ \Ｈ \Ｉ \Ｊ \Ｋ \Ｌ \Ｍ \Ｎ \Ｏ \Ｐ
    ;\Ｑ \Ｒ \Ｓ \Ｔ \Ｕ \Ｖ \Ｗ \Ｘ \Ｙ \Ｚ
    ;\ａ \ｂ \ｃ \ｄ \ｅ \ｆ \ｇ \ｈ \ｉ \ｊ \ｋ \ｌ \ｍ \ｎ \ｏ \ｐ
    ;\ｑ \ｒ \ｓ \ｔ \ｕ \ｖ \ｗ \ｘ \ｙ \ｚ
    })

(def full-width-alphabet?
  #{
    \Ａ \Ｂ \Ｃ \Ｄ \Ｅ \Ｆ \Ｇ \Ｈ \Ｉ \Ｊ \Ｋ \Ｌ \Ｍ \Ｎ \Ｏ \Ｐ
    \Ｑ \Ｒ \Ｓ \Ｔ \Ｕ \Ｖ \Ｗ \Ｘ \Ｙ \Ｚ
    \ａ \ｂ \ｃ \ｄ \ｅ \ｆ \ｇ \ｈ \ｉ \ｊ \ｋ \ｌ \ｍ \ｎ \ｏ \ｐ
    \ｑ \ｒ \ｓ \ｔ \ｕ \ｖ \ｗ \ｘ \ｙ \ｚ
    })

(defn half-width? [c]
  (< (long c) 256))

(defn ok? [line]
  (let [cs (seq line)]
    (and
      (not= "" line)
      (not-any? half-width? cs)
      (not-any? exclude-table cs)
      (not (every? full-width-alphabet? cs))
      true)))

(defn doit [args]
  (loop []
    (when-let [line (read-line)]
      (when (ok? line)
        (println line))
      (recur))))

(doit *command-line-args*)
