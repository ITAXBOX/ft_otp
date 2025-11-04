JAVA := javac
JFLAGS := --release 11
SRC := $(shell find src -name '*.java')
OUT := out
MAIN := com.ftotp.Main
WRAP := ft_otp

all: $(WRAP)

$(WRAP): compile
	@echo "#!/usr/bin/env sh" > $(WRAP)
	@echo "exec java -cp $(OUT) $(MAIN) \"$$@\"" >> $(WRAP)
	@chmod +x $(WRAP)

compile: $(OUT)/.stamp

$(OUT)/.stamp: $(SRC)
	@mkdir -p $(OUT)
	@$(JAVA) $(JFLAGS) -d $(OUT) $(SRC)
	@touch $(OUT)/.stamp

fclean:
	@rm -rf $(OUT) $(WRAP)

.PHONY: all compile fclean