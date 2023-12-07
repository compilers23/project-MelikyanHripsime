
public class Register 
{
	String register;
	String word;
	
	public Register(String register, String word)
	{
		this.register = register;
		this.word = word;
	}
	 
	public void setWord(String word)
	{
		this.word = word;
	}
	
	public String getWord()
	{
		return this.word;
	}
	
	public String getRegister()
	{
		return this.register;
	}
	
}
