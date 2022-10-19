package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;

public class FuncDefMediate {

    public static void analysis() throws IOException {
        FuncTypeMed();
        IdentMediate.analysis();
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                BlockMediate.analysis();
            }else{
                FuncFParamsMediate.analysis();
                if( getWordMed(poiMed).type == Token.RPARENT ){
                    poiMed++;
                    BlockMediate.analysis();
                }
            }
        }
    }
}
