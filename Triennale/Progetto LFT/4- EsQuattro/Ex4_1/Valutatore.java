import java.io.*;
// Il Valutatore valuta le espressioni aritmetiche semplici, facendo riferimento allo schema di traduzione diretto dalla sintassi indicata.
public class Valutatore {
	private Lexer lex;
	private BufferedReader pbr;
	private Token look;
	public Valutatore(Lexer l, BufferedReader br) {
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
		} else error("syntax-directed error");
	}
	public void start() {   //START SYMBOL
		int expr_val=0;
		if(look.tag=='('||look.tag==Tag.NUM){
			expr_val = expr();
			match(Tag.EOF);
			
			System.out.println(expr_val);
		}else {
			error("syntax-directed error start()");
		}
	}
	private int expr() {
		int term_val, exprp_val=0;
		if((look.tag=='(')||(look.tag==Tag.NUM)){
			term_val = term();
			exprp_val = exprp(term_val);
		}else {
			error("syntax-directed error expr()");
		} 	
		return (exprp_val);
	}
	private int exprp(int exprp_i) {
		int term_val, exprp_val=0;
		switch (look.tag) {	
			case '+':
				match('+');
				term_val = term();
				exprp_val = exprp(exprp_i + term_val);
				break;
			case '-':
				match('-');
				term_val=term();
				exprp_val = exprp(exprp_i - term_val);
				break;
			case ')':
			case Tag.EOF:
				exprp_val=exprp_i;
				break;
			default: 
				exprp_val=exprp_i;
				error("syntax-directed error exprp()");	
		}
		return exprp_val;
	}

	private int term() {
		int fact_val, termp_val=0;
		if(look.tag=='('||look.tag==Tag.NUM){
			fact_val=fact();
			termp_val=termp(fact_val);
		}else {
			error("syntax-directed error term()");
		}
		return termp_val;
	}
	
	private int termp(int termp_i) {
		int fact_val, termp_val;
		switch(look.tag){
			case '*':
				match('*');
				fact_val=fact();
				termp_val=termp(termp_i*fact_val);
				break;
			case '/':
				match('/');
				fact_val=fact();
				termp_val=termp(termp_i/fact_val);
				break;
			case '+':
			case '-':
			case ')':
			case Tag.EOF:
				termp_val=termp_i;
				break;
			default:
				termp_val=termp_i;
				error("syntax-directed error termp()");
		}	
		return termp_val;
	}
	
	private int fact() {
		int fact_val;
		if(look.tag=='('){
			match('(');
			fact_val=expr();
			match(')');
		}else if(look.tag==Tag.NUM){
			NumberTok NUM_value=(NumberTok)look;
			fact_val=NUM_value.lexeme;
			match(Tag.NUM);
		}else {
			fact_val=0;
			error("syntax-directed error fact()");
		}
		return fact_val;
	}
	
	public static void main(String[] args) {
		Lexer lex = new Lexer();
		String path = "Input.txt"; // il percorso del file da leggere
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			Valutatore valutatore = new Valutatore(lex, br);
			valutatore.start();
			br.close();
		} catch (IOException e) {e.printStackTrace();}
	}
}