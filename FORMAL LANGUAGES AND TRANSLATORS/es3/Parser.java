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
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error("syntax error MATCH");
        }
    }

    public void start() {
        switch (look.tag) {
        //START->EXPREOF
        case Tag.NUM:
        case '(':
            expr();
            match(Tag.EOF);
            break;
        default:
            error("Syntax Error START");
            break;
        }
    }

    private void expr() {
        switch (look.tag) { 
        //EXPR->TERMEXPRRP
        case Tag.NUM:
        case '(':
            term();
            exprp();
            break;
        default:
            error("Syntax Error EXPR");
            break;
        }
    }

    private void term() {
        switch (look.tag) { 
        //TERM->FACTTERMP
        case Tag.NUM: 
        case '(':
            fact();
            termp();
            break;
        default:
            error("Syntax Error TERM");
            break;
        }
    }

    private void exprp() {
        switch (look.tag) {
            //EXPRP->+TERMEXPRP
            case '+':
                match('+');
                term();
                exprp();
                break;
            //EXPRP->-TERMEXPRP
            case '-': 
                match('-');
                term();
                exprp();
                break;
            //EXPRP->ε
            case ')':
            case Tag.EOF:       
                break;              
            default:
                 error("syntax error EXPRP");
                 break;

        }
    }

    private void termp() {
        switch (look.tag) {
            //TERMP->*FACRTERMP
            case '*':
                match('*');
                fact();
                termp();
                break;
            //TERMP->/FACRTERMP
            case '/': 
                match('/');
                fact();
                termp();
                break;
            //TERMP->ε    
            case '+':
            case '-':
            case ')':
            case Tag.EOF:
                break;
            default:
                error("syntax error TERMP");
                break;
        }
    }

    private void fact() {
        switch(look.tag){
            //FACT->(EXPR)
            case '(': 
                match('(');
                expr();
                match(')');
                break;
            //FACT->NUM    
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("syntax error FACT");
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path="ParserText.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

/* NULL(EXPRP), NULL(TERMP)
 * 
 * FIRST(START)=FIRST(EXPREOF)=FIRST(EXPR)=FIRST(TERM)=FIRST(FACT)=FIRST((EXPR)) U FIRST(NUM)={(,NUM)}
 * FIRST(EXPR)=(,NUM)}
 * FIRST(TERM)=(,NUM)}
 * FIRST(FACT)=(,NUM)}
 * FIRST(EXPRP)=FIRST(+TERMEXPRP) U FIRST(-TERMEXPRP)={+,-}
 * FIRST(TERMP)=FIRST(*FACTTERMP) U FIRST(/FACTTERMP)={*,/}
 * 
 * $ appartiene a FOLLOW(START)
 * {EOF}=FIRST(EOF) ⊆ FOLLOW(EXPR)
 * 
 * FOLLOW(EXPR) ⊆ FOLLOW(EXPRP)
 * {+,-}=FIRST(EXPRP) ⊆ FOLLOW(TERM)
 * FOLLOW(EXPR) ⊆ FOLLOW(TERM)
 * 
 * FOLLOW(EXPRP) ⊆ FOLLOW(TERM)
 * 
 * {*,/}=FIRST(TERMP) ⊆ FOLLOW(FACT)
 * FOLLOW(TERM) ⊆ FOLLOW(TERMP)
 * 
 * {)}=FIRST()) ⊆ FOLLOW(EXPR)
 * 
 *      X        | FOLLOW(X)
 *    START           {$}  
 *    EXPR          {EOF,)}
 *    TERM          {+,-,EOF,)}
 *    EXPRP         {EOF,)}
 *    FACT          {*,/}
 *    TERMP         {+,-,EOF,)}
 *    
 * 
 * GUIDA(START->EXPREOF)=FIRST(EXPREOF)=FIRST(EXPR)={(,NUM}
 * 
 * GUIDA(EXPR->TERMEXPRP)=FIRST(TERMEXPRP)=FIRST(TERM)={(,NUM}
 * 
 * GUIDA(EXPRP->+TERMEXPRP)=FIRST(+TERMEXPRP)=FIRST(+)={+}
 * GUIDA(EXPRP->-TERMEXPRP)=FIRST(-TERMEXPRP)=FIRST(-)={-}
 * GUIDA(EXPRP-> EPSILON)=FIRST(EPSILON) U FOLLOW(EXPRP)={EOF,)}
 * 
 * GUIDA(TERM-> FACTTERMP)=FIRST(FACTTERMP)=FIRST(FACT)={(,NUM}
 * 
 * GUIDA(TERMP->*FACTTERMP)=FIRST(*FACTTERMP)=FIRST(*)={*}
 * GUIDA(TERMP->/FACTTERMP)=FIRST(/FACTTERMP)=FIRST(/)={/}
 * GUIDA(TERMP-> EPSILON)=FIRST(EPSILON) U FOLLOW(TERMP)={+,-,EOF,)}
 * 
 * GUIDA(FACT->(EXPR))=FIRST((EXPR))=FIRST(()={(}
 * GUIDA(FACT->NUM)=FIRST(NUM)={NUM}

 */

