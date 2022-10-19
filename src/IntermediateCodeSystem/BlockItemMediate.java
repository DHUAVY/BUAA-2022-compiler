package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class BlockItemMediate {

    public static int analysis() throws IOException {
        // BlockItem → Decl | Stmt
        int ret;
        if(IntermediateCode.getWordMed(poiMed).type == Token.CONSTTK || IntermediateCode.getWordMed(poiMed).type == Token.INTTK){
            Decl.analysis();
            ret = 0;
        }else{
            ret = Stmt.analysis();
        }
        return ret;
    }

}
