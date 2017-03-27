これは [naist-jdic](http://sourceforge.jp/projects/naist-jdic/) から、特定のエントリのみを抽出/整列/重複除去を行い、各種の自動生成に利用しやすいようにした各種の辞書ファイル(テキストファイル)を生成するプロジェクトです。

主に「ゲーム向けの自動生成」用途を想定しています。

各ファイルのライセンスは元の `naist-jdic` に準拠します(三条項BSDライセンス)。


# ファイル概要

- `dist/` : 完成した辞書ファイルを置く場所
    - `name-sei.txt` : 「名詞,固有名詞,人名,姓」の「ヨミガナ」のみを抽出したもの
    - `name-mei.txt` : 「名詞,固有名詞,人名,名」の「ヨミガナ」のみを抽出したもの
    - `name-general.txt` : 「名詞,固有名詞,人名,一般」の「ヨミガナ」のみを抽出したもの

- `experimental/` : まだ一般利用には厳しい品質のものを置く場所
    - `toponym-all.txt` : 「名詞,固有名詞,地域,一般」の「ヨミガナ」のみを抽出したもの。非常にエントリが多く、このままでは使いづらい
    - `adjective.txt` : 「形容詞」の内、名詞の直前に直に接続できるものを抽出したもの

- `sources/` : 抽出元ファイルを置く場所
    - `naist-jdic-utf8.csv.gz` : オリジナル配布物に含まれている `naist-jdic.csv` の文字コードは`EUC-JP`なので、これを`utf-8`に変換し、gzip圧縮したもの


# Build

`make ...`

- 生成された辞書ファイルから「どの抽出元ファイルから」「どの条件で抽出したか」を知るには、ヘッダに記載されているビルド日時から、このgitリポジトリのMakefileの内容を確認する、しかない。ので、矢鱈とビルドしないようになっている。
    - 改めてビルドし直したい場合は、まず生成済の辞書ファイルを手で削除してから`make`コマンドを実行する事。
    - 手でエントリの取捨選択を行った為、リビルドできない辞書ファイルが一部存在する(どれなのかはMakefileを見る事)。注意。


# 加工メモ

- csvのカラムの意味について
    - `cut`コマンドの`-f`オプションに対する早見表
        1. 表層形
        2. 左文脈ID
        3. 右文脈ID
        4. 単語生起コスト
        5. 品詞
        6. 品詞細分類1(ない場合は`*`)
        7. 品詞細分類2(ない場合は`*`)
        8. 品詞細分類3(ない場合は`*`)
        9. 活用形(ない場合は`*`)
        10. 活用型(ない場合は`*`)
        11. 原形
        12. 読み(カナ表記)
        13. 発音(カナ表記)
        14. 同義エントリ(複数の表層形を`/`で区切った形式。ない場合は空文字列)
        15. xml情報(詳細不明。ない場合は空文字列)
    - 各カラムについてのメモ
        - 例えば、`-f1`が「すずき」「鈴木」である複数のエントリが存在している。これらの同一判定を行うのは基本的に無理。ネオサイタマ風に「読み」を採用し、それをuniqでまとめるぐらいは可能だが…
            - `-f14`「同義エントリ」が存在する場合は、これを使って同一判定を行う事が可能なようだ。しかしこれが存在するエントリは多くない(動詞や形容詞のみ？)
        - 例えば名前の場合、`-f1`「表層形」、`-f12`「読み」、`-f13「発音」はそれぞれ、「かずゑ」「カズヱ」「カズエ」となる。ネオサイタマ風にする事を考える場合は`-f12`を採用するのがよいと思われる
        - `-f4`「単語生起コスト」は小さいほど高優先度となる。これは出現頻度ではないので注意


# TODO

- [mecab-ipadic-NEologd](https://github.com/neologd/mecab-ipadic-neologd) から辞書を生成する事を検討する
    - ライセンスが非常に複雑
        - 本体はapache2だが、辞書データソース提供元が多数あり、それらのライセンスがまちまち
    - データが非常に巨大
        - 「最近の固有名詞を含んだ固有名詞辞書」みたいなものを作る時のみ採用を検討すべき

- 後置句辞書の作成
    - そもそも日本語には「後置句」に相当するものがないのでは…
        - 英語等での「後置句」も、日本語では「前置句」になってしまう


# License

以下は `naist-jdic` のライセンスです。

~~~
Copyright (c) 2009, Nara Institute of Science and Technology, Japan.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
Neither the name of the Nara Institute of Science and Technology
(NAIST) nor the names of its contributors may be used to endorse or
promote products derived from this software without specific prior
written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
~~~


