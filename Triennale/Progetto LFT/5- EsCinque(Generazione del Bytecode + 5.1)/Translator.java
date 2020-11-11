import java.io.*;
/* Traduttore per programmi
prove input :

read(x);
read(y);
while(x>y){
	case when(x>y) {y:=y-x; print(80000)} else x:=x-y
};
print(x);
print(y+100);
while(x>0){x:= x-1; print(x) };
//Questo commento viene ignorato
z := 2
*/
public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;
    public int b_true;
    public int b_false;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() { // procedo con la lettura del token e la stampo a video
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) { // stampo a video l'errore
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) { // se il tag di look è uguale a quello passato come parametro e non siamo alla fine del file vado avanti
        if (look.tag == t) {
            if (look.tag != Tag.EOF) move();
        } else error("syntax error match");
    }

    public void prog() { // start SYMBOL
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag.CASE || look.tag == Tag.WHILE || look.tag == '{') {
            statList();
            match(Tag.EOF);
        } else {
            error("Error nella produzione : Start ( statList )" + look);
        }

        try {
            code.toJasmin(); // chiamo il metodo toJsmin della classe CodeGenerator che metterà in Output.j tutto il bytecode
        } catch (java.io.IOException e) {
            System.out.println("IO error\n");
        }
    }

    public void statList() {
        if (look.tag == Tag.ID || look.tag == Tag.PRINT || look.tag == Tag.READ ||
            look.tag == Tag.CASE || look.tag == Tag.WHILE || look.tag == '{') {
            int label = code.newLabel();
            stat(label);
            code.emitLabel(label);
            statListp();
        } else {
            error("Error nella produzione statList : " + look);
        }
    }

    public void statListp() {
        int label = code.newLabel();

        switch (look.tag) {
            case Tag.EOF:
                break;
            case ';':
                match(';');
                stat(label);
                code.emitLabel(label);
                statListp();
                break;
            case '}':
                break;
            default:
                error("syntax error nella produzione : statListp");
        }
    }

    public void stat(int next) {
        int addr = 0;
        switch (look.tag) {
            case Tag.ID:
                addr = st.lookupAddress(((Word) look).lexeme);
                if (addr == -1) {
                    st.insert(((Word) look).lexeme, addr = count++);
                }
                match(Tag.ID);
                match(Tag.ASSIGN);
                expr();
                code.emit(OpCode.istore, addr);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                expr();
                code.emit(OpCode.invokestatic, 1);
                match(')');
                break;
            case Tag.READ:
                match(Tag.READ);
                match('(');
                if (look.tag == Tag.ID) {
                    addr = st.lookupAddress(((Word) look).lexeme);
                    if (addr == -1) {
                        st.insert(((Word) look).lexeme, addr = count++);
                    }
                    code.emit(OpCode.invokestatic, 0);
                    code.emit(OpCode.istore, addr);
                }
                match(Tag.ID);
                match(')');
                break;
            case Tag.CASE:
                match(Tag.CASE);
                b_false = code.newLabel();
                b_whenlist(b_false, next);
                code.emitLabel(b_false);
                statElse(next);
                break;
            case '{':
                match('{');
                if (look.tag == Tag.READ || look.tag == Tag.CASE || look.tag == Tag.PRINT ||
                    look.tag == Tag.ID || look.tag == Tag.WHILE) {
                    statList();
                    match('}');
                } else if (look.tag == Tag.EOF) {}
                break;

            case Tag.WHILE:
                match(Tag.WHILE);
                match('(');
                int begin = code.newLabel();
                code.emitLabel(begin);
                b_true = code.newLabel();
                b_expr(b_true, next);
                match(')');
                code.emitLabel(b_true);
                stat(next);
                code.emit(OpCode.GOto, begin);
                break;

            default:
                error("ERROR nella Produzione stat() : " + look);
                break;
        }
    }

    public void b_expr(int labelTrue, int labelFalse) {
        Word relop = null;
        if (look.tag == '(' || look.tag == Tag.ID || look.tag == Tag.NUM) {
            expr();
            if (look.tag == Tag.RELOP) {
                relop = (Word) look;
                match(Tag.RELOP);
            }
            expr();
            switch (relop.lexeme) {
                case ">":
                    code.emit(OpCode.if_icmpgt, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
                    break;
                case "<":
                    code.emit(OpCode.if_icmplt, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
                    break;
                case ">=":
                    code.emit(OpCode.if_icmpge, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
                    break;
                case "<=":
                    code.emit(OpCode.if_icmple, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
                    break;
                case "==":
                    code.emit(OpCode.if_icmpeq, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
                    break;
                case "<>":
                    code.emit(OpCode.if_icmpne, labelTrue);
                    code.emit(OpCode.GOto, labelFalse);
            }
        } else {
            error("ERROR nella Produzione termp()");
        }
    }

    public void b_whenlist(int labelFalse, int next) {
        if (look.tag == Tag.WHEN) {
            when_item(labelFalse, next);
            when_listp(labelFalse, next);
        } else error("ERROR nella Produzione b_whenlist");
    }

    public void when_listp(int labelFalse, int next) {
        if (look.tag == Tag.WHEN) {
            when_item(labelFalse, next);
            when_listp(labelFalse, next);
        } else if (look.tag == Tag.EOF || look.tag == Tag.ELSE) {

        } else error("ERROR nella Produzione when_listp");
    }

    public void when_item(int labelFalse, int next) {
        b_true = code.newLabel();
        match(Tag.WHEN);
        match('(');
        b_expr(b_true, labelFalse);
        match(')');
        code.emitLabel(b_true);
        stat(next);
        code.emit(OpCode.GOto, next);
    }

    public void statElse(int next) {
        if (look.tag == Tag.ELSE) {
            match(Tag.ELSE);
            stat(next);
        } else if (look.tag != ';' && look.tag != Tag.EOF) {
            error("ERROR nella Produzione statElse()");
        }
    }


    private void expr() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            term();
            exprp();
        } else {
            error("ERROR nella Produzione expr()");
        }
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                code.emit(OpCode.iadd);
                exprp();
                break;
            case '-':
                match('-');
                term();
                code.emit(OpCode.isub);
                exprp();
                break;
            case '}':
                break;
            default:
                if (look.tag != ')' && look.tag != Tag.EOF && look.tag != Tag.RELOP &&
                    look.tag != ';' && look.tag != Tag.THEN && look.tag != Tag.ELSE)
                    error("ERROR nella Produzione exprp() : " + look);
                break;
        }
    }

    private void term() {
        if (look.tag == '(' || look.tag == Tag.NUM || look.tag == Tag.ID) {
            fact();
            termp();
        } else {
            error("ERROR nella Produzione term() : " + look);
        }
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                code.emit(OpCode.imul);
                termp();
                break;
            case '/':
                match('/');
                fact();
                code.emit(OpCode.idiv);
                termp();
                break;
            case '}':
                break;
            default:
                if (look.tag != '+' && look.tag != '-' && look.tag != ')' && look.tag != Tag.EOF && look.tag != Tag.RELOP &&
                    look.tag != ';' && look.tag != Tag.THEN && look.tag != Tag.ELSE)
                    error("ERROR nella Produzione termp() : " + look);
                break;
        }
    }

    private void fact() {
        switch (look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).lexeme);
                match(Tag.NUM);
                break;
            case Tag.ID:
                int addr = st.lookupAddress(((Word) look).lexeme);
                if (addr == -1) {
                    error("variabile non inizializzata : " + ((Word) look).lexeme);
                }
                code.emit(OpCode.iload, addr);
                match(Tag.ID);
                break;
            default:
                error("ERROR nella Produzione fact()" + look);
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "file.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator expressionTranslator = new Translator(lex, br);
            expressionTranslator.prog();
            System.out.println("Input valido");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}