これは [naist-jdic](http://sourceforge.jp/projects/naist-jdic/) から、特定のエントリのみを抽出/整列/重複除去を行い、各種の自動生成に利用しやすいようにした各種の辞書ファイル(テキストファイル)を生成するプロジェクトです。

主に「ゲーム向けランダム自動生成」用途を想定しています。

各ファイルのライセンスは元の `naist-jdic` に準拠します(三条項BSDライセンス)。


# ファイル書式

基本的には、一行につき一エントリのテキストファイルです。

- `#` ではじまる行はコメント行です。読み飛ばすようにしてください。
- 空行が含まれています。これも読み飛ばしてください。


# ファイル概要

- `dist/` : 完成した辞書ファイルを置く場所
    - `name-sei.txt` : 「名詞,固有名詞,人名,姓」の「ヨミガナ」のみを抽出したもの
    - `name-mei.txt` : 「名詞,固有名詞,人名,名」の「ヨミガナ」のみを抽出したもの
    - `name-general.txt` : 「名詞,固有名詞,人名,一般」の「ヨミガナ」のみを抽出したもの
    - `indeclinable.txt` : 「形容詞」の「体言接続」を個人的判定により厳選したもの
    - `adjective-verb.txt` : 「名詞」の「形容動詞語幹」を個人的判定により厳選したもの

- `beta/` : まだ調整中の、一般利用には厳しい品質のものを置く場所
    - `noun-kanji.txt` : 「名詞,一般」から、漢字のみで構成された名詞を個人的判定により厳選したもの
        - 「総合」と「綜合」の両方が存在する等の問題がある。しかしこれに対応しようとする場合、手動での判断が必要…
    - `noun-exotic.txt` : 「名詞,一般」から、カタカナ表記の外来語を個人的判定により厳選/追加したもの
        - 判定基準が厳密に定まってない為、もう少し決め直す必要がある
    - `noun-katakana.txt` : 「名詞,一般」から、前述の外来語以外のカタカナ単語を個人的判定により厳選したもの
        - まだ未実装
    - `toponym-bigram.txt` : 後述の`toponym-all.txt`からbigramのエントリ数を抽出したファイル。各行は「(二文字) (数値)」の書式。これを使えば地名のランダム生成が可能
        - 「二文字」はbigramエントリ(文字列開始を示す「`^`」と、文字列終端を示す「`$`」の二種類の特殊文字あり)、後の数値はこのbigramの総出現数
    - `toponym-trigram.txt` : 上記のtrigram版

- `sources/` : 抽出元ファイルを置く場所
    - `naist-jdic-utf8.csv.gz` : オリジナル配布物に含まれている `naist-jdic.csv` の文字コードは`EUC-JP`なので、これを`utf-8`に変換し、gzip圧縮したもの

- `work/` : 抽出元ファイルと完成した辞書ファイルの中間状態のファイルを置く場所
    - `toponym-all.txt` : 「名詞,固有名詞,地域,一般」の「ヨミガナ」のみを抽出したもの。`toponym-bigram.txt`等のの生成に利用する
    - `noun-all.txt` : 「名詞,一般」の全エントリ
    - `noun-katakana.map` : ここには「マーク」と「カタカナのみ」で構成された一覧が入っている。それぞれのマークに応じて各ファイルに出力される。
        - `x`なら`noun-exotic.txt`用
        - `k`なら`noun-katakana.txt`用
        - `?`は今のところどの辞書にも採用されない


# Build

`make ...`

- 生成された辞書ファイルから「どの抽出元ファイルから」「どの条件で抽出したか」を知るには、ヘッダに記載されているビルド日時から、このgitリポジトリのMakefileの内容を確認する、しかない。ので、矢鱈とビルドしないようになっている。
    - 改めてビルドし直したい場合は、まず生成済の辞書ファイルを手で削除してから`make`コマンドを実行する事。
    - 手でエントリの取捨選択を行った為、リビルドできない辞書ファイルが一部存在する(どれなのかはMakefileを見る事)。注意。


# 加工メモ

各辞書ファイルのメンテナ向けの情報です。

## 元csvの書式について

`cut`コマンドの`-f`オプションに対する早見表

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

元csvの各カラムについてのメモ

- 例えば、`-f1`が「すずき」「鈴木」である複数のエントリが存在している。これらの同一判定を行うのは基本的に無理。ネオサイタマ風に「読み」を採用し、それをuniqでまとめるぐらいは可能だが…
    - `-f14`「同義エントリ」が存在する場合は、これを使って同一判定を行う事が可能なようだ。しかしこれが存在するエントリは多くない(動詞や形容詞のみ？)
- 例えば名前の場合、`-f1`「表層形」、`-f12`「読み」、`-f13「発音」はそれぞれ、「かずゑ」「カズヱ」「カズエ」となる。ネオサイタマ風にする事を考える場合は`-f12`を採用するのがよいと思われる
- `-f4`「単語生起コスト」は小さいほど高優先度となる。これは出現頻度ではないので注意

## adjective-verb について

- 「名詞」の「形容動詞語幹」を個人的判定により厳選したもの

- 選択基準は「○○な」と続ける際に自然に思えるものを優先。「○○の」と続ける方が自然なものは除外した方がよい可能性が高い
    - 「○○の」は基本的に「形容動詞語幹としてではない使い方の方が一般的な名詞である」という事、`noun`の方で対応すべき
    - ただし末尾が「ア系」で終わるものは発声的な関係でそうなっているので例外扱いした方がよいケースが多いようだ

## noun系について

- nounは以下の属性フラグを複数持つ、と考えて分類できる。それぞれのnoun辞書ファイルの内容は、これらのフラグの論理結合によって統一されているべき(例えば`noun-exotic.txt`なら「外来語フラグ」を持ち「和製読み外来語フラグ」「和製英語フラグ」を持たない、のような感じ)。
    - 除外フラグ(半角文字を含む、ひらがなのみ、送りがなや表記のバリアント(バリアントのあるものはその中の一個のみを採用したい)、等に相当する名詞)
        - 「ひらがなのみ」を除外するのは、ほとんどの「ひらがなのみ」の名詞は漢字表記やカタカナ表記で代用できる為。
    - 漢語フラグ(漢字のみで構成された名詞。中国由来か和製かは問わない。送りがなを含むものはここには含まない)
    - 外来語フラグ(外来語であっても漢語のものは含まず、カタカナ表記のもののみとする)
    - 英単語フラグ(英語かどうか。英語以外が由来の英単語など、判断に迷うものもある)
    - 和製読み外来語フラグ(「カステラ」等、元々の読みから日本語風の読みに変化したもの。基本的に外来語フラグを同時に持つ)
    - 和製英語フラグ(「パソコン」「テレビ」等。カタカナ表記だが外来語フラグは持たない。後述の略語フラグも同時に持つものが多い。また「ズボン」等、由来の分からないカタカナ単語もここに含める)
    - 略語フラグ(この表記とは別に正式名称の名詞が別エントリとして存在するもの)
    - 動植物フラグ(「メカジキ」「リンゴ」等。本来なら漢字表記が可能なものが多く、どちらの表記を採用するか迷うものが多い。また「ライオン」等、外来語フラグを持つものも多い)
    - 鉱物フラグ(「チタン」「ウラン」等、英語読みと語尾が違うものが多い)
    - 特定意味付与フラグ(「アカ」「ヤク」等、本来はカタカナ表記しないような名詞だが、特定の意味を含めている事を明示する為にカタカナ表記されたもの)
    - その他フラグ(上記いずれのフラグも持たないもの。「小型バス」「祭り」等)

- 取捨選択に迷った時の判断基準
    - 上記ルールを再確認する
    - それでも迷うなら、歌の歌詞、大昔のバトル漫画、ゲームの必殺技の名前(の一部)として採用できそうかどうかで判断する
    - それでも迷うなら、母数が大きいなら捨て、母数が少ないなら採用する

## mark抽出方式について

- 各エントリの先頭に「mark文字」と「スペース」の二文字を付け、このmark文字によって、どの辞書ファイルに流し込むかを決める方式
    - これによって、後からの辞書割り当ての変更/修正を容易とする

- mark抽出を行うファイルは `work/*.map` という名前で保存する

- mark文字は辞書ファイル毎に一文字を決める。どれにも属さない/まだ判定していない場合は `?` のmark文字を付与する。

- 「この単語はどの辞書ファイルにも採用しない」と判断した場合、エントリごと削除してもよい。また「この単語があった方がよいがエントリがない」場合もエントリを追加してもよい

- 一つの単語を複数の辞書ファイルに割り当てたい場合は、同名エントリを複数作り、別のmark文字を付与するとよい。最終的にはuniqにかける前提なので、これでも問題ない


# TODO

- `toponym-*.txt` のメンテ
    - trigram以上はエントリ数が膨大になる為、確率の低い分岐は削るようにしたい。しかし何も考えずに `| sort -k2 -r | head -n1000 | sort |` みたいな感じで削れるかどうかはよく分からない。下手にすると「^あ」のエントリはあるのに「あ○」のエントリがない、みたいな事になる

- `work/noun-katakana.map` の仕分けのルールの明確化と仕分け直し

- `indeclinable.txt` の、mark抽出方式への対応

- `adjective-verb.txt` の、mark抽出方式への対応

- 分かりやすい「使い方」の記載

- ビルド番号による区別についての説明を追加

とりあえず対応しない事にしたもの

- [mecab-ipadic-NEologd](https://github.com/neologd/mecab-ipadic-neologd) から辞書を生成する事を検討する
    - ライセンスが非常に複雑
        - 本体はapache2だが、辞書データソース提供元が多数あり、それらのライセンスがまちまち
    - データが非常に巨大
        - 「最近の固有名詞を含んだ固有名詞辞書」みたいなものを作る時のみ採用を検討すべき(おそらく必要になる事はない)

- 後置句辞書の作成
    - そもそも日本語には「後置句」に相当するものがないのでは…
        - 英語等での「後置句」も、日本語では「前置句」になってしまう
    - 考えた結果、複数のprefix辞書を使えるようにする事での対応とした
        - 要は(英語で考えた際の)「後置形容詞」として一つの辞書にまとめた形


# License

以下は、元データである `naist-jdic` のライセンスです。

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

以下は、各ファイルの個別の改変/追加部分に関するライセンスです。

~~~
Copyright (c) 2017 Atsuo Yamada

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

Redistributions of source code must retain the above copyright notice,
this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
Neither the name of the Atsuo Yamada nor the names of its contributors
may be used to endorse or promote products derived from this software
without specific prior written permission.

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

なお、 `dist/` 配下の各ファイルにはヘッダコメントとしてこのライセンス文を埋め込んでいるので、これらのファイルをそのままの形で利用する場合に限り(例えばブラウザゲーム等)、ライセンスを改めて明記する必要はありません(埋め込まれたライセンス文によって、記載の配布条件を既に満たしている為)。

(DBに登録する等して利用する場合は、別途ライセンスの明示が必要となる筈です)




