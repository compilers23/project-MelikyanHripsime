INPUT_FILE_NAME = "testCode.pas"
all:
		javac	MyCompiler.java
		javac	ScannerNew.java
		javac	SyntaxAnalyzer.java
		javac	AssemblyCodeGenerator.java
		javac	Element.java
		javac	Register.java
test:
		java MyCompiler $(INPUT_FILE_NAME)

