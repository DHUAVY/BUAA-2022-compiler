package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class LValMediate {

    public static void analysis() throws IOException {
        // LVal → Ident {'[' Exp ']'}
        if( getWordMed(poiMed).type == Token.IDENFR ){
            poiMed++;
        }
        while( getWordMed(poiMed).type == Token.LBRACK ){
            poiMed++;
            ExpressionMediate.Exp();
            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }
    }
}
