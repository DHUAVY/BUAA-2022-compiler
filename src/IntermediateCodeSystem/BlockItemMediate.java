package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class BlockItemMediate {

    public static void analysis() throws IOException {
        // BlockItem → Decl | Stmt
        if(IntermediateCode.getWordMed(poiMed).type == Token.CONSTTK || IntermediateCode.getWordMed(poiMed).type == Token.INTTK){
            DeclMediate.analysis();
        }else{
            StmtMediate.analysis();
        }
    }

}
