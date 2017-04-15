
.PHONY: help all clean dist beta work dist-clean beta-clean work-clean header

help:
	@echo "See Makefile"

# TODO: このリストは動的に取得したい。
# ;     ただし indeclinable.txt のような、自動生成できないファイルを
#       自動的に除外できなくてはならない。いい扱い方を考える必要がある
DIST_FILES := \
dist/name-general.txt \
dist/name-sei.txt \
dist/name-mei.txt

# TODO: このリストは動的に取得したい。詳細は同上
BETA_FILES := \
beta/noun-exotic.txt \
beta/noun-kanji.txt \
beta/toponym-bigram.txt

# TODO: このリストは動的に取得したい。詳細は同上
WORK_FILES := \
beta/noun-all.txt \
beta/toponym-all.txt

TMP_PATH := tmp

JDIC_GZ_PATH := sources/naist-jdic-utf8.csv.gz

TEMPLATE_HEADER_PATH := template/header.txt


define write-header
echo '# ' > $@
echo '#' `basename $@` `date '+ : %Y/%m/%d %H:%M:%S'` >> $@
echo '# ' >> $@
cat $(TEMPLATE_HEADER_PATH) >> $@
endef

define prepare-tmp
mkdir -p $(TMP_PATH)
endef





dist: $(DIST_FILES)

beta: $(BETA_FILES)

work: $(WORK_FILES)

all: header dist beta work

header:
	$(prepare-tmp)
	$(write-header)
	mv header $(TMP_PATH)/header.txt



# NB: これらは基本的に実行してはならない
#     (詳細は後述)

dist-clean:
	rm -f $(DIST_FILES)

beta-clean:
	rm -f $(BETA_FILES)

work-clean:
	rm -f $(WORK_FILES)

clean: dist-clean beta-clean work-clean






# ヘッダ内のタイムスタンプが重要な意味を持つので、
# 無駄な更新をしないようにする必要がある
# (更新したい場合のみ、事前に手で個別にファイルを消すというルール)

dist/name-general.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,固有名詞,人名,一般,' | cut -d, -f12 | sort | uniq >> $@



dist/name-sei.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,固有名詞,人名,姓,' | cut -d, -f12 | sort | uniq >> $@



dist/name-mei.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,固有名詞,人名,名,' | cut -d, -f12 | sort | uniq >> $@



dist/indeclinable.txt:
	@echo "Dont support auto-generate"

#	$(write-header)
#	gzip -dc $(JDIC_GZ_PATH) | grep ',体言接続,' | cut -d, -f1 | sort | uniq >> $@



dist/adjective-verb.txt:
	@echo "Dont support auto-generate"

#	$(write-header)
#	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,形容動詞語幹,' | cut -d, -f1 | sort | uniq >> $@



beta/noun-exotic.txt:
	$(write-header)
	cat work/noun-katakana.map | lein exec scripts/filter-mark.clj x | sort | uniq >> $@



beta/noun-kanji.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,一般,' | cut -d, -f1 | lein exec scripts/filter-kanji-only.clj | sort | uniq >> $@



beta/toponym-bigram.txt: beta/toponym-all.txt
	$(write-header)
	lein exec scripts/toponym2bigram.clj beta/toponym-all.txt > $@



work/toponym-all.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,固有名詞,地域,一般,' | cut -d, -f12 | sort | uniq >> $@



work/noun-all.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,一般,' | cat | sort | uniq >> $@








