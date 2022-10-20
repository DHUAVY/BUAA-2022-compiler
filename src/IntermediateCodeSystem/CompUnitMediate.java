package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class CompUnitMediate {
    public static void analysis() throws IOException {

        SymbolTableMediate.init();

        while( (getWordMed(poiMed).type == Token.CONSTTK ||
                getWordMed(poiMed + 2 ).type != Token.LPARENT) &&
                (getWordMed(poiMed + 2 ).type != 0 ) ){
            // 0 -> const, 2 !-> (
            DeclMediate.analysis();
        }

        while( getWordMed( poiMed + 1).type != Token.MAINTK && getWordMed( poiMed + 1).type != 0 ){
            FuncDefMediate.analysis();
        }

        MainFuncDefMediate.analysis();
    }
}
