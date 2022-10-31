package GrammaticalSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;

public class ConstInitVal {

    public static void ConstInitValAnalysis() throws IOException {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if( getWord(poi).type == Token.LBRACE ){
            //writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.RBRACE ){
                //writeWord( getWord(poi) );
                poi++;
            }else{
                ConstInitValAnalysis();
                while( getWord(poi).type == Token.COMMA){
                    //writeWord( getWord(poi) );
                    poi++;
                    ConstInitValAnalysis();
                }
                if( getWord(poi).type == Token.RBRACE ){
                    //writeWord( getWord(poi) );
                    poi++;
                }
            }
        }else{
            Expression.ConstExp();
        }
//        writeGrammer("ConstInitVal");
    }
}
