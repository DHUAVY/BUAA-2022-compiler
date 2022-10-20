package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;
import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class IdentMediate {

    public static String analysis() throws IOException {
        if( getWordMed(poiMed).type == Token.IDENFR ){
            poiMed++;
        }
        return getWordMed(poiMed-1).token;
    }
}
