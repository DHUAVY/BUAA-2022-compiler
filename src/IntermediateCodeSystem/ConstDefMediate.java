package IntermediateCodeSystem;

import LexicalSystem.Lexical;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class ConstDefMediate {
    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

        int dim = 0;

        String str = "";
        String ident = "";

        ident = IdentMediate.analysis();

        while( getWordMed(poiMed).type == Token.LBRACK ){ // {

            dim++;
            poiMed++;
            ExpressionMediate.ConstExp();

            if( getWordMed(poiMed).type == Token.RBRACK ){ // }
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){
            poiMed++;
            ConstInitValMediate.analyse();
            if( dim == 0 ){
                str = "var int " + ident + " = " + ConstInitValMediate.initValList[0]+ "\n";
                IntermediateCode.writeIntermediateCode(str);
            }else if( dim == 1 || dim == 2 ){
                str = "arr int " + ident + "[" + ConstInitValMediate.numExp + "]"+ "\n"; // 由于赋值的特殊性，我们可以直接用该值表示数组的大小。
                IntermediateCode.writeIntermediateCode(str);
                for( int i = 0; i < ConstInitValMediate.numExp; i++ ){
                    str = ident + "[" + i + "]" + " = " + ConstInitValMediate.initValList[i]+ "\n";
                    IntermediateCode.writeIntermediateCode(str);
                }
            }
            ConstInitValMediate.numExp = 0;
        }
    }
}
