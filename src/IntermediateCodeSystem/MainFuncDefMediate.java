package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class MainFuncDefMediate {

    public static void analysis() throws IOException {
        // MainFuncDef → 'int' 'main' '(' ')' Block
        String str = "define i32 @main() {";
        IntermediateCode.writeLlvmIr( str, false);

        if( getWordMed(poiMed).type == Token.INTTK ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.MAINTK ){
                poiMed++;
                if( getWordMed(poiMed).type == Token.LPARENT ){
                    poiMed++;
                    if( getWordMed(poiMed).type == Token.RPARENT ){
                        poiMed++;
                        BlockMediate.analysis( true );
                    }
                }
            }
        }
        str = "}";
        IntermediateCode.writeLlvmIr( str, false);
    }
}
