package IntermediateCodeSystem;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ConstInitValMediate {

    public static void ConstInitValAnalysis() throws IOException {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if( getWordMed(poiMed).type == Token.LBRACE ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.RBRACE ){
                poiMed++;
            }else{
                ConstInitValAnalysis();
                while( getWordMed(poiMed).type == Token.COMMA){
                    poiMed++;
                    ConstInitValAnalysis();
                }
                if( getWordMed(poiMed).type == Token.RBRACE ){
                    poiMed++;
                }
            }
        }else{
            ExpressionMediate.ConstExp();
        }
    }
}
