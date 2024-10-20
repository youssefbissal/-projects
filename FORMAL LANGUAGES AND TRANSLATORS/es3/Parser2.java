import java.io.*;

public class Parser2 {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser2(Lexer l, BufferedReader br) {
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
        // If the current token matches the expected token
        if (look.tag == t) {
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error("Syntax error at line " + lex.line + ": Expected " + t + ", found " + look.tag);
        }
    }

    private void prog(){
        switch(look.tag){
            //PROG->STATLISTEOF
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.FOR:
            case Tag.IF:
            case '{':
                statlist();
                match(Tag.EOF);
                break;
            default:
                error("syntax error PROG" + look);
                break;
        }
    }

    private void statlist(){
        switch(look.tag){
            //STATLIST->STATSTATLISTP
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.FOR:
            case Tag.IF:
            case '{':
                stat();
                statlistp();
                break;
            default:
                error("syntax error STATLIST" + look);
                break;
        }
        
    }

    private void statlistp(){
        switch(look.tag){
        //STATLISTP->,STATSTATLISTP
        case ';':
            match(';');
            stat();
            statlistp();
            break;
        //STATLISTP->ε
        case Tag.EOF:
        case '}':
            break;
        default:
            error("syntax error STATLISTP" + look);
            break;
        }
    }

    private void stat(){
        switch(look.tag){
            //STAT->assignASSIGNLIST
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist();
                break;

            //STAT->print(EXPRLIST)
            case Tag.PRINT:
                match(Tag.PRINT);
                match('(');
                exprlist();
                match(')');
                break;

            //STAT->read(IDLIST)
            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist();
                match(')');
                break;

            //STAT->for(STATQ
            case Tag.FOR:
                match(Tag.FOR);
                match('(');
                statq();
                break;

            //STAT->if(BEXPR)STATSTATP
            case Tag.IF:
                match(Tag.IF);
                match('(');
                bexpr();
                match(')');
                stat();
                statp();
                break;

            //STAT->{STATLIST}
            case '{':
                match('{');
                statlist();
                match('}');
                break;

            default: 
                error("syntax error STAT" + look);
                break;
        }

    }

    private void statp(){
        switch(look.tag){
            //STATP->end
            case Tag.END:
                match(Tag.END);
                break;

            //STATP->elseSTATend
            case Tag.ELSE:
                match(Tag.ELSE);
                stat();
                match(Tag.END);
                break;

            default:
                error("syntax error STATP" + look);
                break;
        }

    }

    private void statq(){
        switch(look.tag){
            //STATQ->id:=EXPR;BEXPR)doSTAT
            case Tag.ID:
                match(Tag.ID);
                match(Tag.INIT);
                expr();
                match(';');
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            //STATQ->relopBEXPR)doSTAT
            case Tag.RELOP:
                bexpr();
                match(')');
                match(Tag.DO);
                stat();
                break;

            default:
                error("syntax error STATQ" + look);
                break;
        }

    }

    private void assignlist(){
        switch(look.tag){
            //ASSIGNLIST->[EXPRtoIDLIST]ASSIGNLISTP
            case '[':
                match('[');
                expr();
                match(Tag.TO);
                idlist();
                match(']');
                assignlistp();
                break;
            default:
                error("syntax error ASSIGNLIST" + look);
                break;
        }

    }

    private void assignlistp(){
        switch(look.tag){
            //ASSIGNLISTP->[EXPRto]ASSIGNLISTP
            case '[':
                match('[');
                expr();
                match(Tag.TO);
                idlist();
                match(']');
                assignlistp();
                break;
            //ASSIGNLISTP->ε
            case ';':
            case Tag.EOF:
            case Tag.ELSE:
            case Tag.END:
            case '}':
                break;
            default:
                error("syntax error ASSIGNLISTP" + look);
                break;
        }

    }

    private void idlist(){
        switch(look.tag){
            //IDLIST->idIDLISTP
            case Tag.ID:
                match(Tag.ID);
                idlistp();
                break;
            default:
                error("syntax error IDLIST" + look);
                break;
        }
    }

    private void idlistp(){
        switch(look.tag){
            //IDLISTP->,idIDLISTP
            case ',':
                match(',');
                match(Tag.ID);
                idlistp();
                break;
            //IDLISTP->ε
            case ')':
            case ']':
                break;
            default:
                error("syntax error IDLISTP" + look);
                break;
        }
    }
    private void bexpr(){
        switch(look.tag){
            //BEXPR->relopEXPREXPR
            case Tag.RELOP:
                match(Tag.RELOP);
                expr();
                expr();
                break;
            default:
                error("syntax error BEXPR" + look);
                break;
        }
    }
        
    private void expr(){
        switch(look.tag){
            //EXPR->+(EXPRLIST)
            case '+':
                match('+');
                match('(');
                exprlist();
                match(')');
                break;
            //EXPR->*(EXPRLIST)
            case '*':
                match('*');
                match('(');
                exprlist();
                match(')');
                break;
            //EXPR->-EXPREXPR
            case '-':
                match('-');
                expr();
                expr();
                break;
            //EXPR->/EXPREXPR
            case '/':
                match('/');
                expr();
                expr();
                break;
            //EXPR->ID
            case Tag.ID:
                match(Tag.ID);
                break;
            //EXPR->NUM
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default: 
                error("syntax error EXPR" + look);
                break;
        }

    }
    private void exprlist(){
        switch(look.tag){
            //EXPRLIST->EXPREXPRLISTP
            case '+','-','/','*',Tag.NUM,Tag.ID:
                expr();
                exprlistp();
                break;
            default: 
                error("syntax error EXPRLIST" + look);
                break;
            

        }
    }
    private void exprlistp(){
        switch(look.tag){
            //EXPRLIST->,EXPREXPRLISTP
            case ',':
                match(',');
                expr();
                exprlistp();
                break;
            //EXPRLIST->ε
            case ')':
                break;
            default: 
                error("syntax error EXPRLISTP" + look);
                break;
            
        }


    }
    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path="es3/ParserText32.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser2 parser = new Parser2(lex, br);
            parser.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


/* 

ELIMINAZIONE AMBIGUITA'
⟨prog⟩ ::= ⟨statlist ⟩EOF

⟨statlist ⟩ ::= ⟨stat ⟩⟨statlistp⟩

⟨statlistp⟩ ::= ; ⟨stat ⟩⟨statlistp⟩ | ε

⟨stat ⟩ ::= assign ⟨assignlist ⟩
| print ( ⟨exprlist ⟩)
| read ( ⟨idlist ⟩)
| for( ⟨statq⟩
| if ( ⟨bexpr ⟩) ⟨stat⟩ ⟨statp⟩
| {⟨statlist ⟩}

⟨statq⟩::= ID := ⟨expr ⟩; ⟨bexpr ⟩) do ⟨stat ⟩ | ⟨bexpr ⟩) do ⟨stat ⟩

⟨statp ⟩::= else ⟨stat ⟩end | end

⟨assignlist ⟩ ::= [ ⟨expr ⟩to ⟨idlist ⟩] ⟨assignlistp⟩

⟨assignlistp⟩ ::= [ ⟨expr ⟩to ⟨idlist ⟩] ⟨assignlistp⟩ | ε

⟨idlist ⟩ ::= ID ⟨idlistp⟩

⟨idlistp⟩ ::= , ID ⟨idlistp⟩ | ε

⟨bexpr ⟩ ::= RELOP ⟨expr ⟩⟨expr ⟩

⟨expr ⟩ ::= + ( ⟨exprlist ⟩) | - ⟨expr ⟩⟨expr ⟩
| * ( ⟨exprlist ⟩) | / ⟨expr ⟩⟨expr ⟩
| NUM | ID

⟨exprlist ⟩ ::= ⟨expr ⟩⟨exprlistp⟩

⟨exprlistp⟩ ::= , ⟨expr ⟩⟨exprlistp⟩ | ε
______________________________________________
CALCOLO GUIDE:
NULL(STATLISTP), NULL(ASSIGNLISTP), NULL(IDLISTP), NULL(EXPRLISTP)

FIRST(PROG)=
FIRST(STATLIST)=
FIRST(STAT)={assign,print,read,for,if,{}
FIRST(STATP)={else,end}
FIRST(STATQ)={ID,RELOP}
FIRST(STATLISTP)={;}
FIRST(ASSIGNLIST)=
FIRST(ASSIGNLISTP)={[}
FIRST(IDLIST)={ID}
FIRST(IDLISTP)={,}
FIRST(BEXPR)={RELOP}
FIRST(EXPR)=
FIRST(EXPRLIST)={+,-,*,/,NUM,ID}
FIRST(EXPRLISTP)={,}

$ appartiene a FOLLOW(PROG)

{EOF}=FIRST(EOF) ⊆ FOLLOW(STATLIST)

{;}=FIRST(STATLISTP) ⊆ FOLLOW(STAT)
FOLLOW(STATLIST) ⊆ FOLLOW(STAT)
FOLLOW(STATLIST) ⊆ FOLLOW(STATLISTP)

FOLLOW(STATLISTP) ⊆ FOLLOW(STAT)

FOLLOW(STAT) ⊆ FOLLOW(ASSIGNLIST)
{)}=FIRST()) ⊆ FOLLOW(EXPRLIST)
{)}=FIRST()) ⊆ FOLLOW(IDLIST)
FOLLOW(STAT) ⊆ FOLLLOW(STATQ)             
{)}=FIRST()) ⊆ FOLLOW(BEXPR)
{else,end}=FIRST(STATP) ⊆ FOLLOW(STAT)
FOLLOW(STAT) ⊆ FOLLOW(STATP)
{}}=FIRST(}) ⊆ FOLLOW(STATLIST)

{;}=FIRST(;) ⊆ FOLLOW(EXPR)
FOLLOW(STATQ) ⊆ FOLLOW(STAT)

{end}=FIRST(end) ⊆ FOLLOW(STAT)

{to}=FIRST(to) ⊆ FOLLOW(EXPR)
{]}=FIRST(]) ⊆ FOLLOW(IDLIST)
FOLLOW(ASSIGNLIST) ⊆ FOLLOW(ASSIGNLISTP)

FOLLOW(IDLIST) ⊆ FOLLOW(IDLISTP)

{+,-,*,/,NUM,ID}=FIRST(EXPR) ⊆ FOLLOW(EXPR)
FOLLOW(BEXPR) ⊆ FOLLOW(EXPR)

{,}=FIRST(EXPRLISTP) ⊆ FOLLOW(EXPR)
FOLLOW(EXPRLIST) ⊆ FOLLOW(EXPR)
FOLLOW(EXPRLIST) ⊆ FOLLOW(EXPRLISTP)
FOLLOW(EXPRLISTP) ⊆ FOLLOW(EXPR)

  X             FOLLOW(X)
PROG               {$}
STATLIST           {EOF,}}
STATLISTP          {EOF,}}
STAT               {;,EOF,else,end,}}
STATQ              {;,EOF,else,end,}}
STATP              {;,EOF,else,end,}}
ASSIGNLIST         {;,EOF,else,end,}}
ASSIGNLISTP        {;,EOF,else,end,}}
IDLIST             {),]}
IDLISTP            {),]}
BEXPR              {)}
EXPR               {;,to,+,-,*,/,NUM,ID,),,}
EXPRLIST           {)}
EXPRLISTP          {)}

GUIDA(PROG->STATLISTEOF)=FIRST(STATLIST)={assign,print,read,for,if,{}

GUIDA(STATLIST->STATSTATLISTP)=FIRST(STATLISTP)=FIRST(STAT)={assign,print,read,for,if,{}

GUIDA(STATLISTP->;STATSTALISTP)=FIRST(;STATSTALISTP)={;}
GUIDA(STATLISTP->EPSILON)=FIRST(EPSILON) U FOLLOW(STATLISTP)={EOF,}}

GUIDA(STAT->assignASSIGNLIST)=FIRST(assignASSIGNLIST)={assign}
GUIDA(STAT->print(EXPRLIST))=FIRST(print(EXPRLIST))={print}
GUIDA(STAT->read(IDLIST))=FIRST(read(IDLIST))={read}
GUIDA(STAT->for(STATQ)=FIRST(for)={for}
GUIDA(STAT->ifBEXPR...)=FIRST(if)={if}
GUIDA(STAT->{STATLIST})=FIRST({)={{}

GUIDA(STATQ->ID...)..)=FIRST(ID)={ID}
GUIDA(STATQ->BEXPR)..)=FIRST(BEXPR)={RELOP}

GUIDA(STATP->else..)=FIRST(else)={else}
GUIDA(STATP->end)={end}

GUIDA(ASSIGNLIST->[....)=FIRST([)={[}

GUIDA(ASSIGNLISTP->[....)=FIRST([)={[}
GUIDA(ASSIGNLISTP->EPSILON)=FOLLOW(ASSIGNLISTP)={;,EOF,else,end,}}

GUIDA(IDLIST->ID..)={ID}

GUIDA(IDLISTP->,..)={,}
GUIDA(IDLISTP->EPSILON)=FOLLOW(IDLISTP)={),]}

GUIDA(BEXPR->RELOP..)={RELOP}

GUIDA(EXPR->+....)={+}
GUIDA(EXPR->-....)={-}
GUIDA(EXPR->/....)={/}
GUIDA(EXPR->*....)={*}
GUIDA(EXPR->NUM)={NUM}
GUIDA(EXPR->ID)={ID}

GUIDA(EXPRLIST->EXPREXPRLISTP)=FIRST(EXPREXPRLISTP)=FIRST(EXPR)={+,-,*,/,NUM,ID}

GUIDA(EXPRLISTP->,....)=FIRST(,)={,}
GUIDA(EXPRLISTP->EPSILON)=FOLLOW(EXPRLISTP)={)}

GRAMMATICA LL(1): OK

 */






