package IntermediateCodeSystem;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ConstInitValMediate {

    public static int numExp = 0;
    public static String[] initValList = new String[10000];

    public static void analyse() throws IOException {
        // ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
        if( getWordMed(poiMed).type == Token.LBRACE ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.RBRACE ){
                poiMed++;
            }else{
                analyse();
                while( getWordMed(poiMed).type == Token.COMMA){
                    poiMed++;
                    analyse();
                }
                if( getWordMed(poiMed).type == Token.RBRACE ){
                    poiMed++;
                }
            }
        }else{
            initValList[numExp++] = ExpressionMediate.ConstExp().value;
        }
    }
}
