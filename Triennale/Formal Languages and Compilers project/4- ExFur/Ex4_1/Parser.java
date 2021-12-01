import java.io.*;

public class Parser {
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;
	public Parser(Lexer l, BufferedReader br) {
		lex = l;
		pbr = br;
		move();
	}
	
	void move() {
		look = lex.lexical_scan(pbr);
		System.out.println("token = " + look);
	}
	
	void error(String s) {
		throw new Error("near line " + lex.line + ": " + s);
	}
	
	void match(int t) {
		if (look.tag == t) {
			if (look.tag != Tag.EOF) move();
		} else error("syntax error");
	}
	
	public void prog() {
		switch(look.tag){
			case Tag.ID:
			case Tag.PRINT:
			case Tag.READ:
			case Tag.CASE:
			case Tag.WHILE:
			case '{':
				statlist();
				match(Tag.EOF);
				break;
			default:
				error("syntax error prog()");
		}	
	}
		
	private void stat() {
		switch(look.tag){
			case Tag.ID:
				match(Tag.ID);
				match('=');
				expr();
				break;
			case Tag.PRINT:
				match(Tag.PRINT);
				match('(');
				expr();
				match(')');	
				break;
			case Tag.READ:
				match(Tag.READ);
				match('(');
				match(Tag.ID);
				match(')');
				break;
			case Tag.CASE:
				match(Tag.CASE);
				whenlist();
				match(Tag.ELSE);
				stat();
				/*if(look.tag==Tag.ELSE){
					match(Tag.ELSE);
					stat();
					break;
				}*/
				break;
			case Tag.WHILE:
				match(Tag.WHILE);
				match('(');
				bexpr();
				match(')');
				stat();
				break;
			case '{' :
				match('{');
				statlist();
				match('}');
				break;
			default:
				error("syntax error stat()");
		}
	}
	private void whenlist(){
		switch(look.tag){
			case Tag.WHEN:
				whenitem();
				whenlistp();
				break;
			default:
				error("syntax error whenlist()");
		}
	}
	private void whenlistp(){
		switch(look.tag){
			case Tag.WHEN:
				whenitem();
				whenlistp();
				break;
			case Tag.ELSE:
				break;
			default:
				error("syntax error whenlistp()");
		}
	}
	private void whenitem(){
		switch (look.tag) {
			case Tag.WHEN:
				match(Tag.WHEN);
				match('(');
				bexpr();
				match(')');
				stat();
			default:
				error("syntax error whenitem()");
		}
	}
	
	private void statlist() {   
		switch(look.tag){
			case Tag.ID:
			case Tag.PRINT:
			case Tag.READ:
			case Tag.CASE:
			case Tag.WHILE:
			case '{':
				stat();
				statlistp();
				break;
			default:
				error("syntax error statlist()");
		}
	}
	
	private void statlistp() {   
		switch(look.tag){
			case ';':
				match(';');
				stat();
				statlistp();
				break;
			case '}':
			case Tag.EOF:
				break;
			default:
				error("syntax error statlistp()");
		}
	}
	
	private void bexpr() {  
		switch(look.tag){
			case '(':
			case Tag.NUM:
			case Tag.ID:
				expr();
				match(Tag.RELOP);
				expr();
				break;
			default:
				error("syntax error bexpr()");
		}
	}

	private void expr() {  
		switch(look.tag){
			case '(':
			case Tag.NUM:
			case Tag.ID:
				term();
				exprp();
				break;
			default:
				error("syntax error expr()");
		}
	}
	
	private void exprp() {  
		switch (look.tag) {
			case '+':
				match('+');
				term();
				exprp();
				break;
			case '-':
				match('-');
				term();
				exprp();
				break;
			case ';':
			case ')':
			case Tag.RELOP:
			case Tag.WHEN:
			case Tag.ELSE:
			case Tag.EOF:
			case '}':
				break;
			default: 
				error("syntax error exprp()");	
		}
	}

	private void term() {  
		switch(look.tag){
			case '(':
			case Tag.NUM:
			case Tag.ID:
				fact();
				termp();
				break;
			default:
				error("syntax error term()");
		}
	}

	private void termp() {  
		switch(look.tag){
			case '*':
				match('*');
				fact();
				termp();
				break;
			case '/':
				match('/');
				fact();
				termp();
				break;
			case '+':
			case '-':
			case ')':
			case '}':
			case ';':
			case Tag.RELOP:
			case Tag.WHEN:
			case Tag.ELSE:
			case Tag.EOF:
				break;
			default:
				error("syntax error termp()");
		}
	}

	private void fact() {  
		switch(look.tag){
			case '(':
				match('(');
				expr();
				match(')');
				break;
			case Tag.NUM:
				match(Tag.NUM);
				break;
			case Tag.ID:
				match(Tag.ID);
				break;
			default:
				error("syntax error fact()");
		}
	}

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Input.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser parser = new Parser(lex, br);
			parser.prog();
			System.out.println("Input OK");
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}

}