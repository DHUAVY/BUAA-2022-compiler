package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class InitValMediate {

    public static void analysis() throws IOException {
        // InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
        if( getWordMed(poiMed).type == Token.LBRACE ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.RBRACE ){
                poiMed++;
            }else{
                analysis();
                while( getWordMed(poiMed).type == Token.COMMA){
                    poiMed++;
                    analysis();
                }
                if( getWordMed(poiMed).type == Token.RBRACE ){
                    poiMed++;
                }
            }
        }else{
            ExpressionMediate.Exp();
        }
    }
}
