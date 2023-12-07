import java.util.List;

public class MyCompiler {
	public static void main(String[] args) throws Exception
	{
		if(args.length == 0)
		{
			System.out.println("Error: No arguments!!!");
		}
		else
		{
			List<Cell> a = ScannerNew.get(args[0]);
			Parser.myProgram(a);
		}
	}
}
