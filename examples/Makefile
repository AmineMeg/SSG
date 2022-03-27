EXAMPLES=\
	minimal \
	mini-templates \
	mini-theme \
	templates2
TARGET=examples.zip
REMOTE=irif:public_html/examples-gla.zip

.PHONY: all clean deploy VERSION

all: $(TARGET)

deploy: $(TARGET)
	rsync -avz $< $(REMOTE)

clean:
	rm -f $(TARGET)

$(TARGET): $(shell find $(EXAMPLES) -type f) VERSION
	zip -r -o $@ VERSION $(EXAMPLES)

VERSION:
	date > $@
