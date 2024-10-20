public class Es6 {
    

    //Esercizio 1.6. Modificare l’automa dell’esercizio precedente in modo che riconosca il linguaggio di stringhe (sull’alfabeto {/, *, a}) che contengono “commenti” delimitati da /* e */, ma con la possibilita` di avere stringhe prima e dopo come specificato qui di seguito. L’idea e` che sia possibile avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli dell’alfabeto. 
    //Quindi l’unico vincolo e` che l’automa deve accettare le stringhe in cui un’occorren- za della sequenza /* deve essere seguita (anche non immediatamente) da un’occorrenza della sequenza */. Le stringhe del linguaggio possono non avere nessuna occorrenza della sequenza /* (caso della sequenza di simboli senza commenti). Implementare l’automa seguendo la costru- zione vista in Listing 1.
    //Esempi di stringhe accettate: “aaa/****/aa”, “aa/*a*a*/”, “aaaa”, “/****/”, “/*aa*/”, “*/a”, “a/**/***a”, “a/**/***/a”, “a/**/aa/***/a”
    //Esempi di stringhe non accettate: “aaa/*/aa”, “a/**//***a”, “aa/*aa”

            public static boolean scan(String s){
            int state=0;
            int i=0;
    
            while(state>=0 && i<s.length()){
                final char ch= s.charAt(i++);
    
                switch(state){
                    case 0:
                    if((ch=='/'))
                        state= 1;
                    else if ((ch=='*' || ch=='a'))
                        state= 0;
                    else 
                        state= -1;
                    break;
    
                    case 1:
                    if((ch=='*'))
                        state= 2;
                    else if ((ch=='a'))
                        state= 0;
                    else 
                        state= -1;
                    break;
    
                    case 2:
                    if((ch=='/' || ch=='a'))
                        state= 2;
                    else if ((ch=='*'))
                        state= 3;
                    else 
                        state=-1;
                    break;
    
                    case 3:
                    if((ch=='*'))
                        state= 3; 
                    else if ((ch=='a'))
                        state= 2;
                    else if ((ch=='/'))
                        state= 4;
                    else 
                        state=-1;
                    break;

                    case 4:
                    if((ch=='*' || ch=='a'))
                        state= 4;
                    else if ((ch=='/'))
                        state= 1;
                    else 
                        state=-1;
                    break;
                    
                }
            } return state==4 || state==0 || state==1;
    }
    
        public static void main(String[] args)
        {
        String[] text={"aaa/****/aa", "aa/*a*a*/", "aaaa", "/****/", "/*aa*/", "*/a", "a/**/***a", "a/**/***/a", "a/**/aa/***/a", "aaa/*/aa", "a/**//***a", "aa/*aa"};
        for(int i=0;i<text.length;i++){
        System.out.println(scan(text[i]) ? "OK" : "NOPE");
        }
        }
    }
    
    

