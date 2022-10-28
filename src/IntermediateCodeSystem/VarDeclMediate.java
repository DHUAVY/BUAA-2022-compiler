package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static  IntermediateCodeSystem.ExpAnalyse.*;
import static IntermediateCodeSystem.IntermediateCode.*;

public class VarDeclMediate {

    public static void analysis() throws IOException {
        // VarDecl → BType VarDef { ',' VarDef } ';'
        BTypeMed();

        VarDefMediate.analysis();

        while( getWordMed(poiMed).type == Token.COMMA){
            poiMed++;
            VarDefMediate.analysis();
        }

        if( getWordMed(poiMed).type == Token.SEMICN ){
            poiMed++;
        }
    }
}
