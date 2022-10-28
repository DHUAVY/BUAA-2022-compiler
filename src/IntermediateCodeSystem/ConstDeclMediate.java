package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;
import static  IntermediateCodeSystem.ExpAnalyse.*;
import static IntermediateCodeSystem.IntermediateCode.*;

public class ConstDeclMediate {
    public static void analysis() throws IOException {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'

        int md = mode;
        if( getWordMed(poiMed).type == Token.CONSTTK ){
            poiMed++;
        }

        BTypeMed();

        mode = varMode;
        ConstDefMediate.ConstDefAnalysis();

        while( getWordMed(poiMed).type == Token.COMMA){
            poiMed++;
            ConstDefMediate.ConstDefAnalysis();
        }

        if( getWordMed(poiMed).type == Token.SEMICN ){
            poiMed++;
        }
        mode = md;
    }
}
