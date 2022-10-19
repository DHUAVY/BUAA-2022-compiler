package IntermediateCodeSystem;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;


public class BlockMediate {
    public static void analysis() throws IOException {
        // Block → '{' { BlockItem } '}'
        int ret = 0;
        if( getWordMed(poiMed).type == Token.LBRACE ){ // {
            poiMed++;
            while( getWordMed(poiMed).type != Token.RBRACE ){ // 当下一个字符不为'}'时，进入BlockItem
                BlockItemMediate.analysis();
            }
            poiMed++;
        }
    }
}
