package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class VarDefMediate {

    public static void analysis() throws IOException {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
        IdentMediate.analysis();

        while( getWordMed(poiMed).type == Token.LBRACK ){
            poiMed++;
            ExpressionMediate.ConstExp();
            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){
            poiMed++;
            InitValMediate.analysis();
        }
    }
}
