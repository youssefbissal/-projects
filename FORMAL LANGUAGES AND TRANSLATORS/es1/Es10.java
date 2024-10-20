public class Es10 {
    /*
     * Esercizio 1.10.Progettare e implementare un DFA che riconosca il linguaggio di
        stringhe che contengono il tuo nome e tutte le stringhe ottenute
        dopo la sostituzione di un carattere del nome con un altro qualsiasi.
        • Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA
        • deve accettare la stringa “Paolo” (cioè il nome scritto
        correttamente), ma anche le stringhe “Pjolo”, “caolo”, “Pa%lo”,
        “Paola” e “Parlo”
        (il nome dopo la sostituzione di un carattere)
        • Non deve accettare
        “Eva”, “Perro”, “Pietro” oppure “P*o*o”.
     */

     public static boolean scan(String s){
        int i=0;
        int state=0;
        while(state>=0 && i<s.length()){
            final char ch= s.charAt(i++);
            switch(state){
                case 0:
                if(ch=='B')
                    state= 1;
                else if(!(Character.isWhitespace(ch)))
                    state= 7;
                else 
                    state= -1;
                break;

                case 1:
                if(ch=='i')
                    state= 2;
                else if(!(Character.isWhitespace(ch)))
                    state= 8;
                else 
                    state= -1;
                break;

                case 2:
                if(ch=='s')
                    state= 3;
                else if(!(Character.isWhitespace(ch)))
                    state= 9;
                else 
                    state= -1;
                break;

                case 3:
                if(ch=='s')
                    state= 4;
                else if(!(Character.isWhitespace(ch)))
                    state= 10;
                else 
                    state= -1;
                break;
                
                case 4:
                if(ch=='a')
                    state= 5;
                else if(!(Character.isWhitespace(ch)))
                    state= 11;
                else 
                    state= -1;
                break;

                case 5:
                if(!(Character.isWhitespace(ch)))
                    state= 6;
                else 
                    state= -1;
                break;

                case 6:
                    state= -1;
                break;

                case 7:
                if((ch=='i'))
                    state= 8;
                else 
                    state= -1;
                break;

                case 8:
                if((ch=='s'))
                    state= 9;
                else 
                    state= -1;
                break;

                case 9:
                if((ch=='s'))
                    state= 10;
                else 
                    state= -1;
                break;

                case 10:
                if((ch=='a'))
                    state= 11;
                else 
                    state= -1;
                break;

                case 11:
                if((ch=='l'))
                    state= 6;
                else 
                    state= -1;
                break;

            }
        }
        return state==6;}
        public static void main(String[] args)
        {
        String[] text={"Bissal","*issal",")is)al"};
        for(int i=0;i<text.length;i++){
        System.out.println(scan(text[i]) ? "OK" : "NOPE");
        }
        }
    }

