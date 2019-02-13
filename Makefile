build: antlr jflex java interp tokens

antlr: ./src/grammar/IMPGrammar.g4
	cp ./src/grammar/IMPGrammar.g4 IMPGrammar.g4
	java -cp ./lib/antlr-MagicTool.jar org.antlr.v4.Tool -Werror -visitor -no-listener -o ./src/antlr -package antlr IMPGrammar.g4
	rm IMPGrammar.g4

jflex: ./src/lexer/Parser.flex
	jflex -d ./src/lexer ./src/lexer/Parser.flex -v

java:
	mkdir ./bin
	find -name "*.java" > sources.txt
	javac -d ./bin -cp ./lib/antlr-MagicTool.jar @sources.txt 
	rm sources.txt

interp:
	cp ./src/antlr/*.interp ./bin/antlr/
	
tokens:
	cp ./src/antlr/*.tokens ./bin/antlr/

run:
	java -cp ./bin:./lib/antlr-MagicTool.jar:. Main
clean:
	rm -fr ./src/lexer/Parser.java*
	rm -fr ./src/antlr/IMPGrammar*
	rm -fr ./bin
