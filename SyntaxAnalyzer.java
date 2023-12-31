import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
	
	static int index = 0;
	static List<String> symbolTable = new ArrayList<>();
	
	public static boolean findInSymbolTable(String string)
	{

		for (String s : symbolTable) {
			if (string.equals(s)) {
				return false;
			}
		}
		return true;
	}
	
	public static void myProgram(List<Element> list) throws Exception {
		AssemblyCodeGenerator.generateProgramStart();
		header(list);
		defs(list);
		AssemblyCodeGenerator.generateNewLines(2);
		if(!list.get(index).getWord().equals("begin"))
		{
			throw new Exception();
		}
		else
		{
			AssemblyCodeGenerator.generateBegin();
			++index;
		}
		statements(list);
		if(!list.get(index).getWord().equals("end"))
		{
			throw new Exception();

		}
		else
		{
			++index;
		}
		if(!list.get(index).getWord().equals("."))
		{
			throw new Exception();
		}
		AssemblyCodeGenerator.generateNewLines(2);
		AssemblyCodeGenerator.generateEnd();
	}
	
	public static void defs(List<Element> list) throws Exception
	{
		if(list.get(index).getWord().equals("var"))
		{
			++index;
			statement(list);
			while(!list.get(index).getWord().equals("begin"))
			{
				statement(list);
			}
		}
	}
	
	public static void header(List<Element> list) throws Exception
	{
		if(!list.get(index).getWord().equals("program"))
		{
			throw new Exception("Error: Not supported");
		}
		else
		{
			++index;
		}
		
		if(!list.get(index).getType().equals("identifier"))
		{
			throw new Exception("Error");
		}
		else
		{
			if(findInSymbolTable(list.get(index).getWord()))
			{
				symbolTable.add(list.get(index).getWord());
				++index;
			}
			else
			{
				throw new Exception();
			}
		}
		if(!list.get(index).getWord().equals(";"))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}

	}
	
	public static void statement(List<Element> list) throws Exception
	{
		
		if(!list.get(index).getType().equals("identifier"))
		{
			throw new Exception("Error: Identifier is expected!");
		}
		else
		{
			if(findInSymbolTable(list.get(index).getWord()))
			{
				symbolTable.add(list.get(index).getWord());
				AssemblyCodeGenerator.generateDeclaration(list.get(index).getWord());
				++index;
			}
			else
			{
				throw new Exception("Repeating identifier!");
			}
			if(list.get(index).getWord().equals(","))
			{
				++index;
				if(!list.get(index).getType().equals("identifier"))
				{
					throw new Exception("Error: Identifier is expected!");
				}
				else
				{
					if(findInSymbolTable(list.get(index).getWord()))
					{
						symbolTable.add(list.get(index).getWord());
						AssemblyCodeGenerator.generateDeclaration(list.get(index).getWord());
						++index;
					}
					else
					{
						throw new Exception();
					}
				}
				while(!list.get(index).getWord().equals(":"))
				{
					if(!list.get(index).getWord().equals(",") || !list.get(index +1).getType().equals("identifier"))
					{
						throw new Exception();
					}
					else
					{
						if(findInSymbolTable(list.get(index + 1).getWord()))
						{
							symbolTable.add(list.get(index +1).getWord());
							AssemblyCodeGenerator.generateDeclaration(list.get(index +1).getWord());
						}
						else
						{
							throw new Exception();
						}
						index += 2;
					}
				}
			}
			
		}
		if(!list.get(index).getWord().equals(":"))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}
		type(list);
		if(!list.get(index).getWord().equals(";"))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}
	}
	
	public static void type(List<Element> list) throws Exception
	{
		if(index >= list.size())
		{
			throw new Exception("Error: type is expected!");
		}
		if(list.get(index).getWord().equals("integer") || list.get(index).getWord().equals("string"))
		{
			++index;
		}
		else
		{
			throw new Exception("Error");
		}
	}
	
	public static void statements(List<Element> list) throws Exception
	{
		if(index >= list.size())
		{
			throw new Exception("Error");
		}
		while(!list.get(index).getWord().equals("end"))
		{
			if(list.get(index +3).getWord().equals(";"))
			{
				simpleAssignment(list);
			}
			else
			{
				complexAssignment(list);
			}
		}
	}
	
	public static void simpleAssignment(List<Element> list) throws Exception
	{
		String word;
		if(index >= list.size())
		{
			throw new Exception("Error");
		}
		if( index + 3 >= list.size())
		{
			throw new Exception("Error");
		}
		word = getString(list);
		Element cell = operand(list);
		String operand = cell.getWord();
		String type = cell.getType();
		if(!list.get(index).getWord().equals(";"))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}
		AssemblyCodeGenerator.generateSimpleAssignment(word, operand, type);
	}

	private static String getString(List<Element> list) throws Exception {
		String word;
		if(!list.get(index).getType().equals("identifier"))
		{
			throw new Exception("Error:An identifier is expected!");
		}
		else
		{
			if(findInSymbolTable(list.get(index).getWord()))
			{

				throw new Exception("The identifier is not Defined!");
			}

			 word = list.get(index).getWord();
			++index;
		}
		if(!list.get(index).getWord().equals(":="))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}
		return word;
	}

	public static void complexAssignment(List<Element> list) throws Exception
	{
		String word;
		String op1;
		String op2;
		if(index >= list.size())
		{
			throw new Exception("Error:complexAssignment is expected!");
		}
		if( index + 5 >= list.size())
		{
			throw new Exception("Error:complexAssignment is expected!");
		}

		word = getString(list);

		op1 = operand(list).getWord();
		String operation = mathOperator(list);
		op2 = operand(list).getWord();
		
		if(!list.get(index).getWord().equals(";"))
		{
			throw new Exception("Error: Invalid Symbol");
		}
		else
		{
			++index;
		}
		AssemblyCodeGenerator.generateComplexAssignment(word,op1,op2,operation);
	}

	public static Element operand(List<Element> list) throws Exception
	{
		if(index >= list.size())
		{
			throw new Exception("Error:Invalid operation!");
		}
		if(list.get(index).getType().equals("identifier")|| list.get(index).getType().equals("number"))
		{
			if(list.get(index).getType().equals("identifier"))
			{
				if(findInSymbolTable(list.get(index).getWord()))
				{
					throw new Exception("The identifier is not Defined!");
				}
			}
			++index;
			return list.get(index -1);
		}
		else
		{
			throw new Exception("Error: invalid expression!");
		}
	}
	
	public static String mathOperator(List<Element> list) throws Exception
	{
		if(index >= list.size())
		{
			throw new Exception("Error: invalid operation");
		}
		if(list.get(index).getWord().equals("+") || list.get(index).getWord().equals("-"))
		{
			++index;
			return list.get(index -1).getWord();
		}
		else
		{
			throw new Exception("Error: invalid expression!");
		}
	}
}
