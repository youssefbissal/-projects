import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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
        } else error("Syntax error");
    }

    public void prog() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.FOR || look.tag == Tag.IF || look.tag == '{') {
            int lnext_prog = code.newLabel();
            statlist(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (IOException e) {
                System.out.println("IO error\n");
            }
        }else
            error("Error prog!!");
    }

    private void statlist(int lnext_prog) {
        switch (look.tag) {
            case Tag.ASSIGN:
            case Tag.PRINT:
            case Tag.READ:
            case Tag.FOR:
            case Tag.IF:
            case '{':
                int lnext_stat = code.newLabel();
                stat(lnext_stat);
                code.emitLabel(lnext_stat);
                statlistp();
                code.emit(OpCode.GOto, lnext_prog);
                break;
            default:
                error("Error statlist!!");
        }
    }

    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(Token.semicolon.tag);
                int lnext_stat = code.newLabel();
                stat(lnext_stat);
                code.emitLabel(lnext_stat);
                statlistp();
                break;
            case Tag.EOF:
            case '}':
                break;
            default:
                error("Error statlistp!!");
        }
    }

    public void stat(int lnext_stat) {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                assignlist();
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.PRINT:
                match(Tag.PRINT);
                match(Token.lpt.tag);
                exprlist(OpCode.invokestatic);
                match(Token.rpt.tag);
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.READ:
                match(Tag.READ);
                match('(');
                idlist(1);
                match(')');
                code.emit(OpCode.GOto, lnext_stat);
                break;

            case Tag.FOR:
                match(Tag.FOR);
                match(Token.lpt.tag);
                int loop = code.newLabel();
                int cond_true = code.newLabel();
                staq();
                code.emitLabel(loop);
                bexpr(cond_true, lnext_stat);
                match(Token.rpt.tag);
                match(Tag.DO);
                code.emitLabel(cond_true);
                stat(loop);
                break;

            case Tag.IF:
                match(Tag.IF);
                match(Token.lpt.tag);
                int if_true = code.newLabel();
                int if_false = code.newLabel();
                int if_false1 = code.newLabel();
                bexpr(if_true, if_false);
                match(Token.rpt.tag);
                code.emitLabel(if_true);
                stat(lnext_stat);
                code.emitLabel(if_false);
                stap(if_false1);
                code.emitLabel(if_false1);
                match(Tag.END);
                break;

            case '{':
                match(Token.lpg.tag);
                statlist(lnext_stat);
                match(Token.rpg.tag);
                break;

            default:
                error("Error stat!!");
        }

    }

    private void assignlist() {
        switch (look.tag) {
            case '[':
                match(Token.lpq.tag);
                expr();
                match(Tag.TO);
                idlist(0);
                match(Token.rpq.tag);
                assignlistp();
                break;
            default:
                error("Error assignlist!!");
        }
    }

    private void assignlistp() {
        switch (look.tag) {
            case '[':
                match(Token.lpq.tag);
                expr();
                match(Tag.TO);
                idlist(0);
                match(Token.rpq.tag);
                assignlistp();
                break;
            case ';':
            case Tag.EOF:
            case '}':
            case Tag.END:
            case Tag.ELSE:
                break;
            default:
                error("Error assignlistp!!");
        }
    }

    private void staq() {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                match(Word.init.tag);
                expr();
                match(Token.semicolon.tag);

                code.emit(OpCode.istore, id_addr);
                break;
            case Tag.RELOP:
                break;
            default:
                error("Error staq!!");
        }
    }

    private void stap(int iffalse1) {
        switch (look.tag) {
            case Tag.ELSE:
                match(Tag.ELSE);
                stat(iffalse1);
                break;
            case Tag.END:
                break;
            default:
                error("Error stap!!");
        }
    }
    //Method responsible for parsing boolean expressions, particularly those involving relational operators (e.g., <, >, ==, etc.).
    private void bexpr(int lnext_true, int lnext_false) {
        if (look.tag == Tag.RELOP) {
            switch (look.tag) {
                case Tag.RELOP:
                    String relop = ((Word) look).lexeme; //It extracts the relational operator lexeme
                    match(Tag.RELOP);
                    expr();
                    expr();
                    switch (relop) {
                        case ">":
                            code.emit(OpCode.if_icmpgt, lnext_true);
                            break;
                        case "<":
                            code.emit(OpCode.if_icmplt, lnext_true);
                            break;
                        case "==":
                            code.emit(OpCode.if_icmpeq, lnext_true);
                            break;
                        case ">=":
                            code.emit(OpCode.if_icmpge, lnext_true);
                            break;
                        case "<=":
                            code.emit(OpCode.if_icmple, lnext_true);
                            break;
                        case "<>":
                            code.emit(OpCode.if_icmpne, lnext_true);
                            break;
                    }
                    break;
            }
            code.emit(OpCode.GOto, lnext_false);
        } else
            error("Error bexpr!!");
    }

    private void idlist(int read) {
        switch (look.tag) {
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                if(read == 1)
                    code.emit(OpCode.invokestatic,0);
                idlistp(read);
                code.emit(OpCode.istore, id_addr);
                break;
            default:
                error("Error idlist!!");
        }
    }

    private void idlistp(int read) {
        switch (look.tag) {
            case ',':
                match(Token.comma.tag);
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                match(Tag.ID);
                if(read == 1)
                    code.emit(OpCode.invokestatic,0);
                idlistp(read);
                code.emit(OpCode.istore, id_addr);
                break;
            case ']':
            case ')':
                break;
            default:
                error("Error idlistp!!");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match(Token.plus.tag);
                match(Token.lpt.tag);
                exprlist(OpCode.iadd);
                match(Token.rpt.tag);
                break;
            case '*':
                match(Token.mult.tag);
                match(Token.lpt.tag);
                exprlist(OpCode.imul);
                match(Token.rpt.tag);
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '/':
                match(Token.div.tag);
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).num);
                match(Tag.NUM);
                break;
            case Tag.ID:
                int address = st.lookupAddress(((Word) look).lexeme);
                if (address == -1) {
                    address = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                code.emit(OpCode.iload, address);
                match(Tag.ID);
                break;
            default:
                error("Error expr!!");
        }
    }

    private void exprlist(OpCode operation) {
        switch (look.tag) {
            case '+':
            case '*':
            case '-':
            case '/':
            case Tag.NUM:
            case Tag.ID:
                expr();
                exprlistp(operation);
                break;
            default:
                error("Error exprlist!!");
        }
    }

    private void exprlistp(OpCode operation) {
        switch (look.tag) {
            case ',':
                match(Token.comma.tag);

                if (operation == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                    expr();
                } else {
                    expr();
                    code.emit(operation);
                }

                exprlistp(operation);
                break;
            case ')':
                if (operation == OpCode.invokestatic) {
                    code.emit(OpCode.invokestatic, 1);
                }
                break;
            default:
                error("Error exprlistp!!");
        }
    }


    public static void main(String[] args) {
        Lexer lex = new Lexer(); 
        
        String path = "es5/Translator.txt"; 

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            Translator traduttore = new Translator(lex, br);

            traduttore.prog();

            System.out.println("Input OK");

            br.close();
        } catch (IOException e) {e.printStackTrace();}
    }
}
