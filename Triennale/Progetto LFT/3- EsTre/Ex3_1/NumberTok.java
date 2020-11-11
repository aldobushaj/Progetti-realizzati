public class NumberTok extends Token{
	public static int lexeme=0;
	public NumberTok(int tag, int stringa) {
		super(tag);
		lexeme=stringa; 
	}
	
	public String toString(){
		return "<"+tag+", "+lexeme+">";
	}
}