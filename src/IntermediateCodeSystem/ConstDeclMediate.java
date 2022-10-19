package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;

public class ConstDeclMediate {
    public static void analysis() throws IOException {
        // ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'

        if( getWordMed(poiMed).type == Token.CONSTTK ){
            poiMed++;
        }

        BTypeMed();

        ConstDef.ConstDefAnalysis();

        while( getWordMed(poiMed).type == Token.COMMA){
            poiMed++;
            ConstDef.ConstDefAnalysis();
        }

        if( getWordMed(poiMed).type == Token.SEMICN ){
            poiMed++;
        }
    }
}
