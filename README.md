これは [naist-jdic](http://sourceforge.jp/projects/naist-jdic/) から、特定のエントリのみを抽出/整列/重複除去を行い、各種の自動生成に利用しやすいようにした各種の辞書ファイル(テキストファイル)を生成するプロジェクトです。

各ファイルのライセンスは元の `naist-jdic` に準拠します(三条項BSDライセンス)。


# ファイル概要

- `naist-jdic-utf8.csv.gz` : オリジナル配布物に含まれている `naist-jdic.csv` の文字コードは`EUC-JP`なので、これを`utf-8`に変換し、gzip圧縮したもの。ここから抽出を開始する

- `dist/` : 完成した辞書ファイルを置く場所
    - `sei.txt` : 「名詞,固有名詞,人名,姓」の「カナ表記」のみを抽出したもの
    - `mei.txt` : 「名詞,固有名詞,人名,名」の「カナ表記」のみを抽出したもの
    - `name-ippan.txt` : 「名詞,固有名詞,人名,一般」の「カナ表記」のみを抽出したもの
    - `toponym-all.txt` : 「名詞,固有名詞,地域,一般」の「カナ表記」のみを抽出したもの。非常にエントリが多く、このままでは使いづらい


# 加工メモ

- 元csvの人名系カラムの意味について
    - `単語, 左文脈ID, 右文脈ID, 単語生起コスト, 名詞, 固有名詞, 人名, (姓|名|一般), *, *, (単語と同じ？), カナ表記, 読みカナ, '', ''`
        - 「単語」、「カナ表記」、「読みカナ」の違いは「かずゑ」「カズヱ」「カズエ」の差
        - 「単語生起コスト」は小さいほど高優先度(not出現頻度)
        - 「単語」のところは「すずき」「鈴木」両方が登録されている
            - 扱いが結構難しい、「カナ表記」で統一する等して扱うのが簡単
    - 人名以外についても大体似たような感じだと思われる

- 生成手順について
    - 基本的には `gzip -dc naist-jdic-utf8.csv.gz | grep '名詞,固有名詞,人名,一般' | cut -d, -f12 | sort | uniq > dist/name-ippan.txt` みたいに抽出してから、ヘッダ部を追加する
    - 実際の生成時はワンライナーではなく、一時ファイルに書き出して少しずつ処理を進めるとよい


# License

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


