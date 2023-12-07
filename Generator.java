import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Generator {
	private static final List<Register> registers = new ArrayList<>();
	private static final File file = new File("testHripsime.s");
	private static String lastAssigned;

	static {
		registers.add(new Register("rax", ""));
		registers.add(new Register("rbx", ""));
		registers.add(new Register("rcx", ""));
		registers.add(new Register("rdx", ""));
		registers.add(new Register("r8", ""));
		registers.add(new Register("r9", ""));
		registers.add(new Register("r10", ""));
		registers.add(new Register("r11", ""));
		registers.add(new Register("r12", ""));
		registers.add(new Register("r13", ""));
		registers.add(new Register("r14", ""));
		registers.add(new Register("r15", ""));
	}

	public static String findRegister(String identifier) {
		for (Register register : registers) {
			if (register.getWord().isEmpty()) {
				register.setWord(identifier);
				return register.getRegister();
			}
		}
		return "";
	}

	public static String findFullRegister(String identifier) {
		for (Register register : registers) {
			if (register.getWord().equals(identifier)) {
				return register.getRegister();
			}
		}
		return "";
	}

	public static void generateProgramStart() throws IOException {
		boolean result = file.createNewFile();
		if (result) {
			System.out.println("File created: " + file.getCanonicalPath());
		} else {
			System.out.println("File already exists at location: " + file.getCanonicalPath());
		}
		FileWriter myWriter = new FileWriter(file);
		myWriter.write(".data" + "\n" + "\n");
		myWriter.close();
	}

	public static void generateDeclaration(String name) throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		myWriter.write(".comm " + name + ", " + 8 + "\n");
		myWriter.close();
	}

	public static void generateNewLines(int count) throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		while (count > 0) {
			myWriter.write("\n");
			count--;
		}
		myWriter.close();
	}

	public static void generateBegin() throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		myWriter.write(".text" + "\n" + ".globl _start" + "\n" + "\n" + "_start:" + "\n");
		myWriter.close();
	}

	public static void generateSimpleAssignment(String word, String operand, String type) throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		if (type.equals("number")) {
			myWriter.write("movq $" + operand + ", " + word + "\n");
		} else {
			myWriter.write("movq " + operand + ", " + word + "\n");
		}
		String register = findRegister(word);
		myWriter.write("movq " + word + ", %" + register + "\n");
		lastAssigned = word;
		myWriter.close();
	}

	public static void generateComplexAssignment(String word, String op1, String op2, String operation) throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		String r1 = findFullRegister(op1);
		String r2 = findFullRegister(op2);
		String r = "";

		if (r1.isEmpty() && !r2.isEmpty()) {
			if (operation.equals("+")) {
				myWriter.write("add $" + op1 + ", %" + r2 + "\n");
				r = r2;
			} else {
				String r3 = findRegister(op1);
				myWriter.write("movq $" + op1 + ", %" + r3 + "\n");
				myWriter.write("sub %" + r2 + ", %" + r3 + "\n");
				r = r3;
			}
		} else if (!r1.isEmpty() && r2.isEmpty()) {
			if (operation.equals("+")) {
				myWriter.write("add $" + op2 + ", %" + r1 + "\n");
				r = r1;
			} else {
				myWriter.write("sub $" + op2 + ", %" + r1 + "\n");
				r = r1;
			}
		} else if (r1.isEmpty() && r2.isEmpty()) {
			String r3 = findRegister(op1);
			myWriter.write("movq $" + op1 + ", %" + r3 + "\n");
			if (operation.equals("+")) {
				myWriter.write("add $" + op2 + ", %" + r3 + "\n");
				r = r3;
			} else {
				myWriter.write("sub $" + op2 + ", %" + r3 + "\n");
				r = r3;
			}
		} else {
			if (operation.equals("+")) {
				myWriter.write("add %" + r1 + ", %" + r2 + "\n");
				r = r2;
			} else {
				myWriter.write("sub %" + r2 + ", %" + r1 + "\n");
				r = r1;
			}
		}

		myWriter.write("movq %" + r + ", " + word + "\n");

		if (findFullRegister(word).isEmpty()) {
			findRegister(word);
		}

		myWriter.close();
		lastAssigned = word;
	}

	public static void generateEnd() throws IOException {
		FileWriter myWriter = new FileWriter(file, true);
		myWriter.write("exit(0)" + "\n" + "mov $60, %rax" + "\n" + "mov " + lastAssigned + ", %rdi" + "\n" +
				"syscall");
		myWriter.close();
	}
}
