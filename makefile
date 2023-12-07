INPUT_FILE_NAME = "testCode.pas"
all:
		javac	MyCompiler.java
		javac	ScannerNew.java
		javac	Parser.java
		javac	Generator.java
		javac	Cell.java
		javac	Register.java
test:
		java MyCompiler $(INPUT_FILE_NAME)

