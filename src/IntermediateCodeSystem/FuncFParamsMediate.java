package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class FuncFParamsMediate {

    public static void analysis( FunctionMediate fun ) throws IOException {
        // FuncFParams → FuncFParam { ',' FuncFParam }

        FuncFParamMediate.analysis(  fun  );
        while( getWordMed(poiMed).type == Token.COMMA ){
            IntermediateCode.writeLlvmIrWord(", ", false);
            poiMed++;
            FuncFParamMediate.analysis( fun );
        }
    }
}
