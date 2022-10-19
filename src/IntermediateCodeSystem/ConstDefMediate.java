package IntermediateCodeSystem;

import LexicalSystem.Lexical;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ConstDefMediate {
    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

        Ident.ident();

        while( getWordMed(poiMed).type == Token.LBRACK ){ // {
            poiMed++;
            Expression.ConstExp();

            if( getWordMed(poiMed).type == Token.RBRACK ){ // }
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){
            poiMed++;
        }

        ConstInitVal.ConstInitValAnalysis();

    }
}
