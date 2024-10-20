import java.io.*;


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
        throw new Error("near line " + Lexer.line + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
    	    if (look.tag != Tag.EOF) move();
    	} else {
            error("Syntax Error in match()");
        }
    }

    public void start() { 
	    int expr_val;
        if(look.tag == '(' || look.tag==Tag.NUM){
            expr_val = expr();
            match(Tag.EOF);
            System.out.println("Result: " + expr_val);

        } else {
            error("Syntax Error in start()");
        }
    }


    private int expr() { 
	    int term_val, exprp_val;
        if(look.tag == '(' || look.tag==Tag.NUM){
            term_val = term();
            exprp_val = exprp(term_val);
            return exprp_val;
        } else {
            error("Syntax Error in expr()");
        }
        return -1;
    }


    private int exprp(int exprp_i) {
		int term_val, exprp_val;
		switch (look.tag) {
			case '+':
				match('+');
				term_val = term();
				exprp_val = exprp(exprp_i + term_val);
				return exprp_val;
			case '-':
				match('-');
				term_val = term();
				exprp_val = exprp(exprp_i - term_val);
				return exprp_val;
            case ')':
            case Tag.EOF:
                return exprp_i;
			default:
				error("Syntax Error in exprp()");
		}
		return -1;
    } 


    private int term() { 
        int fact_val, termp_val;
        if(look.tag == '(' || look.tag==Tag.NUM){
            fact_val = fact();
            termp_val = termp(fact_val);
            return termp_val;
        } else {
            error("Syntax Error in term()");
        }
        return -1;
    }
    

    private int termp(int termp_i) { 
        int fact_val, termp_val;
        switch(look.tag){
            case '*': {
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                return termp_val;
            }
            case '/': {
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i / fact_val);
                return termp_val;
            }
            case '+':
            case '-':
            case ')':
            case Tag.EOF: {
                return termp_i;
            }
            default: {
                error("syntax error in termp().");
            }
        }
        return -1;
    }
    
    private int fact() { 
        int num_value, expr_val;
        if(look.tag == '('){
            match('(');
            expr_val = expr();
            match(')');
            return expr_val;
        }
        else if(look.tag == Tag.NUM){
            num_value = ((NumberTok)look).num;
            match(Tag.NUM);
            return num_value;
        } else {
            error("syntax error in fact().");
        }
        return -1;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "es4/Valutatore.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
