public class Es2
{
    /*
    Esercizio 1.2. 
    Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
    in un linguaggio in stile Java: un identificatore `e una sequenza non vuota di lettere, numeri, ed il
    simbolo di “underscore” _ che non comincia con un numero e che non pu `o essere composto solo
    dal simbolo _. 
    Compilare e testare il suo funzionamento su un insieme significativo di esempi.
    Esempi di stringhe accettate: “x”, “flag1”, “x2y2”, “x_1”, “lft lab”, “ temp”, “x 1 y 2”,
    “x_”, “_5”
    Esempi di stringhe non accettate: “5”, “221B”, “123”, “9_to_5”, “___"
    */

    public static boolean scan(String s){
    int state=0;
    int i=0;
    while(state>=0 && i<s.length()){
        final char ch= s.charAt(i++);


        switch(state){
            case 0:
            if((ch >='a' && ch<='z') || (ch >='A' && ch<='Z'))
                state= 2;
            else if(ch=='_')
                state= 1;
            else
                state= -1;
            break;

            case 1:
            if((ch=='_'))
                state= 1;
            else if((ch >='a' && ch<='z') || (ch >='A' && ch<='Z') || (ch >='0' && ch<='9'))
                state= 2;
            else 
                state= -1;
            break;

            case 2:
            if((ch=='_') || (ch >='a' && ch<='z') || (ch >='A' && ch<='Z') || (ch >='0' && ch<='9'))
                state= 2;
            else 
                state= -1;
            break;


            
        }

    } return state==3;
  }
  public static void main(String[] args)
  {
  System.out.println(scan("") ? "OK" : "NOPE");
  }
}

