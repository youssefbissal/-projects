public class Es7 {
    /*
     *Esercizio 1.7. Modificare l’automa dell’esercizio 1.3 in modo che riconosca le combinazioni di matricola e
        cognome di studenti del turno 2 o del turno 3 del laboratorio, dove il numero di matricola e il
        cognome:
        • possono essere separati da una sequenza di spazi (simbolo ws - spazio)
        • possono essere precedute e/o seguite da sequenze eventualmente vuote di spazi.
        • Per esempio, l’automa
        – deve accettare la stringa “654321 Rossi ” e “ 123456 Bianchi ” (dove, nel secondo
        esempio, ci sono spazi prima del primo carattere e dopo l’ultimo carattere)
        – ma non “1234 56Bianchi ” e “123456Bia nchi ”.
     */
            public static boolean scan(String s){
            int state=0;
            int i=0;
    
            while(state>=0 && i<s.length()){
                final char ch= s.charAt(i++);
    
                switch(state){
                    case 0:
                    if(ch==' ')
                        state=0;
                    else if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 2;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 1;
                    else 
                        state= -1;
                    break;
    
                    case 1:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 2;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 1;
                    else if((ch==' '))
                        state= 3; 
                    else if (ch>='L' && ch<='Z')
                        state= 5;
                    else 
                        state= -1;
                    break;
    
                    case 2:
                    if(((ch-'0')%2==0) && (ch>='0' && ch<='9'))
                        state= 2;
                    else if (((ch-'0')%2!=0) && (ch>='0' && ch<='9'))
                        state= 1;
                    else if((ch==' '))
                        state= 4; 
                    else if((ch>='A' && ch<='K'))
                        state= 5;
                    else 
                        state=-1;
                    break;
    
                    case 3:
                    if((ch==' '))
                        state= 3; 
                    else if (ch>='L' && ch<='Z')
                        state= 5;
                    else 
                        state=-1;
                    break;

                    case 4:
                    if((ch==' '))
                        state= 3; 
                    else if ((ch>='A' && ch<='K'))
                        state= 5;
                    else 
                        state=-1;
                    break;

                    case 5:
                    if((ch>='a' && ch<='z'))
                        state= 5; 
                    else if ((ch>='A' && ch<='Z'))
                        state= 7;
                    else if ((ch==' '))
                        state= 6;
                    else 
                        state=-1;
                    break;

                    case 6:
                    if((ch==' '))
                        state= 6; 
                    else if ((ch>='A' && ch<='Z'))
                        state= 7;
                    else 
                        state=-1;
                    break;

                    case 7:
                    if((ch==' '))
                        state= 6; 
                    else if ((ch>='a' && ch<='z'))
                        state= 5;
                    else if ((ch>='A' && ch<='Z'))
                        state= 7;
                    else 
                        state=-1;
                    break;
                
                    
                }
            } return state==5 || state==6 || state==7;
    }
    
        public static void main(String[] args)
        {
            String[] text={"654321 Rossi ", " 123456 Bianchi ","1234 56Bianchi ","123456Bia nchi "};
            for(int i=0;i<text.length;i++){
            System.out.println(scan(text[i]) ? "OK" : "NOPE");
            }
        }
    }
    
    

