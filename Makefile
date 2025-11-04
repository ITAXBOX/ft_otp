JAVA := javac
JFLAGS := --release 11
OUT := out
MAIN := com.ftotp.Main
WRAP := ft_otp

all: $(WRAP)

$(WRAP): compile
	@echo '#!/bin/sh' > $(WRAP)
	@echo 'exec java -cp $(OUT) $(MAIN) "$$@"' >> $(WRAP)
	@chmod +x $(WRAP)

compile:
	@mkdir -p $(OUT)
	@find src -name '*.java' -exec $(JAVA) $(JFLAGS) -d $(OUT) {} +

fclean:
	@rm -rf $(OUT) $(WRAP)

.PHONY: all compile fclean