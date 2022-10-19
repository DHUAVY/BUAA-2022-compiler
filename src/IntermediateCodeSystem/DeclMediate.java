package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class DeclMediate {

    public static void analysis() throws IOException {
        // Decl → ConstDecl | VarDecl
        if( getWordMed(poiMed).type == Token.CONSTTK ) {
            ConstDeclMediate.analysis();
        }else{
            VarDeclMediate.analysis();
        }
    }
    
}
