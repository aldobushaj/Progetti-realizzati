import java.io.*;
public class Parser {
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;

	public Parser(Lexer l, BufferedReader br) {
		lex = l;
		pbr = br;
		move();//leggo il token con lexe
	}
	void move() {
		look = lex.lexical_scan(pbr);// leggo con il lexer che token è
		System.out.println("token = " + look);
	}
	void error(String s) {
		throw new Error("near line " + lex.line + ": " + s);
	}
	void match(int t) {
		if (look.tag == t) {
		if (look.tag != Tag.EOF) move();// se il file non è finito leggo il prossimo carattere
		} else error("syntax error");
	}
	
	public void start() {
		if(look.tag=='('||look.tag==Tag.NUM){//insiemi guida dello start simble
			expr();
			match(Tag.EOF);
		}else {
			error("syntax error start");
		}
	}

	private void expr() {
		if((look.tag=='(')||(look.tag==Tag.NUM)){
			term();
			exprp();
		}else {
			error("syntax error expr");
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
		case ')':
			break;
		case Tag.EOF:
			break;
		default: 
			error("syntax error exprp");	
		}
	}

	private void term() {
		if(look.tag=='('||look.tag==Tag.NUM){
			fact();
			termp();
		}else {
			error("syntax error term");
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
			break;
		case '-':
			break;
		case ')':
			break;
		case Tag.EOF:
			break;
		default:
			error("syntax error termp");
		}
	}

	private void fact() {
		if(look.tag=='('||look.tag==Tag.NUM){
			if(look.tag=='('){
				match('(');
				expr();
				match(')');
			}else match(Tag.NUM);
		}else {
			error("syntax error fact");
		}
	}

	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Input.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Parser parser = new Parser(lex, br);
			parser.start();
			System.out.println("Input OK");
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}