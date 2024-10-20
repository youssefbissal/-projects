public class Es8 {
    /*  Esercizio 1.8.Progettare e implementare un DFA che, come in
        Esercizio 1.3, riconosca il linguaggio di stringhe che
        contengono matricola e cognome di studenti del turno 2
        o del turno 3 del laboratorio, ma in cui il cognome
        precede il numero di matricola
        – in altre parole, le posizioni del cognome e matricola sono
        scambiate rispetto all’Esercizio 1.3) */

        public static boolean scan(String s){
            int state=0;
            int i=0;
    
            while(state>=0 && i<s.length()){
                final char ch= s.charAt(i++);
    
                switch(state){
                    case 0:
                    if((ch>='L' && ch<='Z'))
                        state= 1;
                     else if((ch>='A' && ch<='K'))
                        state= 2;
                    else 
                        state= -1;
                    break;
                    
                    case 1:
                    if((ch>='a' && ch<='z'))
                        state= 1;
                    else if (((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 4;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 3;
                    else 
                        state= -1;
                    break;

                    case 2:
                    if((ch>='a' && ch<='z'))
                        state= 2;
                    else if (((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 4;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 5;
                    else 
                        state= -1;
                    break;

                    case 3:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 4;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 3;
                    else 
                        state= -1;
                    break;
    
                    case 4:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 4;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 3;
                    else 
                        state=-1;
                    break;

                    case 6:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 6;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 5;
                    else 
                        state= -1;
                    break;
    
                    case 5:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 6;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 5;
                    else 
                        state=-1;
                    break;
    
                    
                
                }
            } return state==3 || state==6;
    }

    public static void main(String[] args)
        {
            String[] text={"Rossi731899","Bianchi731899"};
            for(int i=0;i<text.length;i++){
            System.out.println(scan(text[i]) ? "OK" : "NOPE");
            }
        }
}  
