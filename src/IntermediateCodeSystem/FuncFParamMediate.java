package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;

public class FuncFParamMediate {

    public static void analysis() throws IOException {
        // FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        BTypeMed();
        IdentMediate.analysis();
        if( getWordMed(poiMed).type == Token.LBRACK ){

            poiMed++;
            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;

                while( getWordMed(poiMed).type == Token.LBRACK ){
                    poiMed++;
                    ExpressionMediate.ConstExp();
                    if( getWordMed(poiMed).type == Token.RBRACK ){
                        poiMed++;
                    }
                }

            }

        }
    }
}
