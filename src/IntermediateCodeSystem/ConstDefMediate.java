package IntermediateCodeSystem;

import LexicalSystem.Lexical;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ConstDefMediate {
    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

        IdentMediate.analysis();

        while( getWordMed(poiMed).type == Token.LBRACK ){ // {
            poiMed++;
            ExpressionMediate.ConstExp();

            if( getWordMed(poiMed).type == Token.RBRACK ){ // }
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){
            poiMed++;
        }

        ConstInitValMediate.ConstInitValAnalysis();

    }
}
