import java.lang.String;
import java.io.*;
import java.util.*;
public class Lexer {
	public static int line = 1;
	private char peek = ' ';
private void readch(BufferedReader br) {
	try{
		peek = (char) br.read();
	}catch (IOException exc) {	
		peek = (char) -1; // ERROR
	}
}
public Token lexical_scan(BufferedReader br) {
	while(peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
		if(peek == '\n') line++;
		readch(br);
	}
	
	
	
	switch(peek) {
		case '!': peek = ' ';
				return Token.not;
		case '(': peek =' ';
				return Token.lpt;
		case')': peek=' ';
				return Token.rpt;
		case '+': peek=' ';
				return Token.plus;
		case '-': peek=' ';
				  return Token.minus;
		case '*': peek=' ';
				return Token.mult;
		case ';': peek=' ';
		return Token.semicolon;
		
	
		case '&': readch(br);
				if (peek == '&') {
						peek = ' ';
				return Word.and;
				}
				else{
					System.err.println("Erroneous character"+ " after & : "  + peek );
					return null;
				}
		case '|': readch(br);
				  if(peek=='|') {
					  peek=' ';
					  return Word.or;
				  }
				  else {
					System.err.println("Erroneous character"+ "after |: " + peek);  
					return null;
				  }
		case '<': readch(br);
				  if(peek == '=') {
					  peek=' ';
					  return Word.le;
				  }
				  else if (peek=='>'){
				  				peek=' ';
				  				return Word.ne;
				  		}
				  else if(peek==Tag.EOF){
					  System.err.println("Erroneous character"+ "after <"+ peek);
					  return null;}
				  else {
				  		
				  			return Word.lt;
				  		}
				  
				  
				  
				  
		case '>': readch(br);
				  if(peek=='=') {
					  peek=' ';
					  return Word.ge;
				  }
				  else if(peek==Tag.EOF){
					  System.err.println("Erroneous character"+ "after >"+ peek);
					  return null;
					  }
				  else {
					  
					  return Word.gt;
				  }
		case ':':
			readch(br);
			if(peek=='=') {
				peek=' ';
				return Word.assign;
			}else {
				System.err.println("Erroneous character" + " after = : " + peek );
				return null;
		}
		case '=':
			readch(br);
			if(peek=='=') {
				peek=' ';
				return Word.eq;
			}else {
				System.err.println("Erroneous character" + " after = : " + peek );
				return null;
		}
				  		
		case '/': readch(br);
				  if(peek=='*'){
					  readch(br);
					  char prec=peek;
					  while(prec!= '*' || peek!='/'){
						  if (peek== (char)-1){
							  System.out.println("Errore: commento non chiuso");
							  return null;
						  }
						  prec=peek;
						  readch(br);
					  }
					  peek=' ';
					  return this.lexical_scan(br);
				  }
				  else if (peek=='/'){
					  readch(br);
					  while(peek!='\n'){
						 if (peek== (char)-1){
							 return new Token(Tag.EOF);
						}
						  readch(br);
					  }
					  readch(br);
					  return this.lexical_scan(br);
				  }
				  else return Token.div;
		case (char)-1: return new Token(Tag.EOF);
		default:if(Character.isLetter(peek) || peek=='_') {

			String s = "";
			while(Character.isLetter(peek)|| Character.isDigit(peek) || peek=='_') {
				s=s+peek;
				readch(br);
			}
				
			if(s.equals("then"))
				return Word.then;
			else if(s.equals("else"))
				return Word.elsetok;
			else if(s.equals("when"))
				return Word.when;
			else if(s.equals("while"))
				return Word.whiletok;
			else if(s.equals("case"))
				return Word.casetok;
			else if(s.equals("do"))
				return Word.dotok;
			else if(s.equals("print"))
				return Word.print;
			else if(s.equals("read"))
				return Word.read;
			else if(s.equals("_")){
					System.out.println("Errore nella dichiarazione della variabile");
					return null;
			}
			else return  new Word(Tag.ID,s);
		}
		else if (Character.isDigit(peek)) {
// ... gestiti il caso dei numeri ... //
			String s="";
			while(Character.isDigit(peek)) {
				s=s+peek;
				readch(br);
			}
			return new NumberTok(Tag.NUM,Integer.parseInt(s));
		}
		else{
				System.err.println("Erroneous character: "+ peek );
				return null;
			}
	}
	
	
}
public static void main(String[] args) {
	Lexer lex =new Lexer();
	String path = "Input.txt"; // il percorso del file da leggere
	try{
		BufferedReader br =new BufferedReader(new FileReader(path));
		Token tok;
		do{
			tok = lex.lexical_scan(br);
			System.out.println("Scan: " + tok);
		}while(tok.tag != Tag.EOF);
		br.close();
	}catch (IOException e) {e.printStackTrace();}
}
}