package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class FuncRParamsMediate {

    public static void analysis() throws IOException {
        // FuncRParams → Exp { ',' Exp }
        ExpressionMediate.Exp();

        while(getWordMed(poiMed).type == Token.COMMA){
            poiMed++;
            ExpressionMediate.Exp();
        }

    }
}
