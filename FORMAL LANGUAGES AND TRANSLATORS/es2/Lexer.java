import java.io.*;
import java.util.*;



public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            System.out.println(peek);
            peek = (char) -1; // ERROR
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') line++;
            readch(br);
        }

        switch (peek) {
            case '!': peek = ' '; return Token.not;           
            case '(': peek = ' '; return Token.lpt;         
            case ')': peek = ' '; return Token.rpt;        
            case '[': peek = ' '; return Token.lpq;
            case ']': peek = ' '; return Token.rpq;
            case '{': peek = ' '; return Token.lpg;            
            case '}': peek = ' '; return Token.rpg;
            case '+': peek = ' '; return Token.plus;
            case '-': peek = ' '; return Token.minus;
            case '*': peek = ' '; return Token.mult;
            case '/':

            //Let's read the next char to understand if could be a divison, a comment or just a single /
            readch(br);
           
            //After / there are spaces, tabs, etc...
             while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
                if (peek == '\n') line++;
                readch(br);
        }
    
    // If the next character is a letter, digit, or the end of the file, it is considered as the division symbol, and the corresponding token is returned
    if (Character.isLetterOrDigit(peek) || peek == (char) -1) {
         peek = ' ';
        return Token.div;
    } 
    // Otherwise it is a comment. 
    else {
        switch (peek) {
            // Single-line comment
            case '/':
                do {
                    // Skip characters until the end of the line
                    readch(br);
                    if(peek==(char)-1) return lexical_scan(br);
                } while (peek != '\n'); 
                break;
            
            // Multi-line comment
            case '*':
                do{          
                    // Skip characters until '*/' is encountered         
                        do{
                           readch(br);
                           if(peek==(char)-1) return lexical_scan(br);
                        }while(peek!='*');

                        while(peek=='*')
                        {
                            readch(br);
                            if(peek==(char)-1) return lexical_scan(br);
                        }
                }while(peek!='/');
                break;
            
            // Error for unrecognized comments
            default:
                System.err.println("Erroneous comment" + (int)peek);
                return null;
        }
        
        // Move to the next character after the comment
        readch(br);
        return lexical_scan(br);
    
    }
        

            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

            //If the current character is '&' and the next character is also '&', it consumes both characters, sets peek to a space, and returns the logical AND token. 
            //Otherwise, it prints an error.
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character after & : "  + peek );
                    return null;
                }

            //If the current character is '|' and the next character is also '|', it consumes both characters, sets peek to a space, and returns the logical OR token. 
            //Otherwise, it prints an error.
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character after | : "  + peek );
                    return null;
                }
                
            
            case '>':
                readch(br);
                if (peek != '=') {
                    peek = ' ';
                    return Word.gt;
                } else {
                    peek = ' ';
                    return Word.ge;
                }

            //Handles greater than, less than, greater than or equal to, less than or equal to, and not equal to. 
            //It reads the next character and checks for combinations like >=, <=, and !=, returning the corresponding tokens.
            case '<': readch(br);
					if(peek=='='){
                        peek = ' '; return Word.le;
                    }
					else if(peek=='>'){
							peek = ' '; 
							return Word.ne;
                    }
					else {
							peek = ' '; 
							return Word.lt;
                    }


            
                //If the current character is ':' and the next character is '=', it consumes both characters, sets peek to a space, and returns the assignment token.
                //Otherwise, it prints an error.
                case ':':
                    readch(br); 
                    if(peek=='=') {
                        peek = ' '; 
                        return Word.init;
                    }
                    else{ 
                        System.err.println("Erroneous character after : : "  + peek );
                         return null;
                    }

                //Handles identifiers starting with an underscore. It collects characters until a non-letter or non-digit character is encountered. 
                //If there's at least one non-underscore character, it returns an identifier token. 
                //Otherwise, it prints an error.    
                case '_': 
                boolean IDorKEY= false;
                String s="";
                do{
                    s+=peek;
                    if(peek!='_') IDorKEY= true;
                    readch(br);
                }
                while(peek=='_' || Character.isLetterOrDigit(peek));
                                
                    if(IDorKEY)
                        return new Word(Tag.ID,s);
                    else 
                        System.err.println("Erroneous character after '_' : "  + s);
                        return null;
            
                //If the end of the file is reached, it returns an EOF token.
                case (char) -1:
                    return new Token(Tag.EOF);

                default:
                    if (Character.isLetter(peek)) {
                        // Process identifier or keyword
                        String IDorKey = "";
                        do {
                            IDorKey += peek;
                            readch(br);
                        } while (Character.isLetterOrDigit(peek) || peek == '_');
                
                        switch (IDorKey) {
                            // Check if the identifier is a keyword
                            case "assign": return Word.assign;
                            case "to": return Word.to;
                            case "if": return Word.iftok;
                            case "else": return Word.elsetok;
                            case "do": return Word.dotok;
                            case "for": return Word.fortok;
                            case "begin": return Word.begin;
                            case "end": return Word.end;
                            case "print": return Word.print;
                            case "read": return Word.read;
                            default: return new Word(Tag.ID, IDorKey);
                        }
                    } else if (Character.isDigit(peek)) {
                        // Process numeric literal
                        String Number = "";
                        boolean Zero = false;
                        if(peek=='0') {Zero=true;} 
                        do {
                            Number+=peek;
                            readch(br);
                                if(Zero){
                                    if(Character.isDigit(peek)){
                                        System.err.println("Error");
                                        return null;
                                    }
                                }
                        } while (Character.isDigit(peek));
                        return new NumberTok(Tag.NUM, Integer.parseInt(Number));
                    } else {
                        // Handle erroneous character
                        System.err.println("Erroneous character: " + peek);
                        return null;
                    }
                
            }
        }

    public static void main(String[] args) {
        //Creates an object Lexer with fields line and peak, rappreseting the current line and the current read char
        Lexer lex = new Lexer(); 
        //The path of the file that should be read
        File path = new File("es2/Lexer.txt"); 
        try {
            //Creates a BufferedReader object to read from a file specified by the path
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
          catch (NullPointerException i) {
            System.out.println("Input di testo non valido alla linea " + line);
        }
            
    }
}