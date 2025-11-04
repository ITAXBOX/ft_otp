JAVA := javac
JFLAGS := --release 11
OUT := out
MAIN := com.ftotp.Main
WRAP := ft_otp
LIB := lib
# Classpath including external jars
# core and javase from ZXing library for QR code generation
CP := $(LIB)/core-3.5.3.jar:$(LIB)/javase-3.5.3.jar

all: $(WRAP)

$(WRAP): compile
	@echo '#!/bin/sh' > $(WRAP)
	@echo 'exec java -cp $(OUT):$(CP) $(MAIN) "$$@"' >> $(WRAP)
	@chmod +x $(WRAP)

compile:
	@mkdir -p $(OUT)
	@find src -name '*.java' | xargs $(JAVA) $(JFLAGS) -cp $(CP) -d $(OUT)

fclean:
	@rm -rf $(OUT) $(WRAP)

.PHONY: all compile fclean