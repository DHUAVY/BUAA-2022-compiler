package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class FuncFParamsMediate {

    public static void analysis() throws IOException {
        // FuncFParams → FuncFParam { ',' FuncFParam }

        FuncFParamMediate.analysis();
        while( getWordMed(poiMed).type == Token.COMMA ){
            poiMed++;
            FuncFParamMediate.analysis();
        }
    }
}
