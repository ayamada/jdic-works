
.PHONY: help all

help:
	@echo "See Makefile"

# タイムスタンプが意味を持つので、無駄な更新をしないようにする必要がある
# (更新したい場合は事前に手でファイルを消すというルールにする)
all: ;


TMP_PATH := tmp

JDIC_GZ_PATH := sources/naist-jdic-utf8.csv.gz

TEMPLATE_HEADER_PATH := template/header.txt

define write-header
echo '# ' > $@
date '+# build : %Y/%m/%d %H:%M:%S' >> $@
echo '# ' >> $@
cat $(TEMPLATE_HEADER_PATH) >> $@
endef

define prepare-tmp
mkdir -p $(TMP_PATH)
endef




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









experimental/toponym-all.txt:
	$(write-header)
	gzip -dc $(JDIC_GZ_PATH) | grep ',名詞,固有名詞,地域,一般,' | cut -d, -f12 | sort | uniq >> $@









