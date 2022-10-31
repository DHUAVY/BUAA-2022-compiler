package GrammaticalSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;
public class InitVal {

    public static void InitValAnalysis() throws IOException {
        // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        if( getWord(poi).type == Token.LBRACE ){
            //writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.RBRACE ){
                //writeWord( getWord(poi) );
                poi++;
            }else{
                InitValAnalysis();
                while( getWord(poi).type == Token.COMMA){
                    //writeWord( getWord(poi) );
                    poi++;
                    InitValAnalysis();
                }
                if( getWord(poi).type == Token.RBRACE ){
                    //writeWord( getWord(poi) );
                    poi++;
                }
            }
        }else{
            Expression.Exp();
        }
//        writeGrammer("InitVal");
    }
}
