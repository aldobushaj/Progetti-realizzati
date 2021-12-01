import java.io.*;
import java.util.*;

public class Lexer {

	public static int line = 1;
	private char peek = ' ';

	private void readch(BufferedReader br) {
		try {
			peek = (char) br.read();
		} catch (IOException exc) {
			peek = (char) -1; // ERROR
		}
	}
	
	public Token lexical_scan(BufferedReader br) {
		while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
			if (peek == '\n') line++;
			readch(br);
		}
		
		
		switch (peek) {
			case '!':
				peek = ' ';
				return Token.not;
			case '(':
				peek = ' ';
				return Token.lpt;
			case ')':
				peek=' ';
				return Token.rpt;
			case '+':
				peek = ' ';
				return Token.plus;
			case '-': 	
				peek = ' ';
				return Token.minus;
			case '*':
				peek = ' ';
				return Token.mult;
			case '/':
				peek = ' ';
				return Token.div;
			case ';':
				peek = ' ';
				return Token.semicolon;
				
		// ... gestire i casi di (, ), +, -, *, /, ; ... //

			case '&':
				readch(br);
				if (peek == '&') {
					peek = ' ';
					return Word.and;
				} else {
					System.err.println("Erroneous character" + " after & : " + peek );
					return null;
				}
			case '|':
				readch(br);
				if(peek=='|') {
					peek=' ';
					return Word.or;
				} else {
					System.err.println("Erroneous character" + " after | : " + peek );
					return null;
				}
			case '<':
				readch(br);
				if(peek=='=') {
					peek=' ';
					return Word.le;
				}else if(peek=='>') {
					peek=' ';
					return Word.ne;
				}else if(Character.isLetter(peek)||Character.isLetter(peek))
					return Word.lt;
				else {
					System.err.println("Erroneous character" + " after < : " + peek );
					return null;
				}
			case '>':
				readch(br);
				if(peek=='=') {
					peek=' ';
					return Word.ge;
				}else if(Character.isLetter(peek)||Character.isLetter(peek)) 
					return Word.gt;
				else { 
					System.err.println("Erroneous character" + " after > : " + peek );
					return null;
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
		// ... gestire i casi di ||, <, >, <=, >=, ==, <>, := ... // 

			case (char)-1:
				return new Token(Tag.EOF);
			default:
				if (Character.isLetter(peek)) {
					String Letter = "";
					while(Character.isLetter(peek)) {
						Letter=Letter+peek;
						readch(br);
					}
					if(Letter.equals("then")) {
						peek=' ';
						return Word.then;
					}else if(Letter.equals("else")) {
						peek=' ';
						return Word.elsetok;
					}else if(Letter.equals("case")) {
						peek=' ';
						return Word.casetok;
					}else if(Letter.equals("when")) {
						peek=' ';
						return Word.when;
					}else if(Letter.equals("while")) {
						peek=' ';
						return Word.whiletok;
					}else if(Letter.equals("do")) {
						peek=' ';
						return Word.dotok;
					}else if(Letter.equals("print")) {
						peek=' ';
						return Word.print;
					}else if(Letter.equals("read")) {
						peek=' ';
						return Word.read;
					}else 
						return new Word(Tag.ID,Letter);
			// ... gestire il caso degli identificatori e delle parole chiave //
			
					}else if (Character.isDigit(peek)) {
						String string="";
						while(Character.isDigit(peek)) {
							string=string+peek;
							readch(br);
						}
						return new NumberTok(Tag.NUM, Integer.parseInt(string));
					} else { 
						String s="";
						System.err.println("Erroneous character" + " after number : " + peek );
						return new Word(Tag.EOF,s);
					}
		// ... gestire il caso dei numeri ... //
				}
		}

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Input.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Token tok;
			do {
				tok = lex.lexical_scan(br);
				System.out.println("Scan: " + tok);
			} while (tok.tag != Tag.EOF);
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}
