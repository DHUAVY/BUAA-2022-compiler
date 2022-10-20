package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class VarDefMediate {

    public static void analysis() throws IOException {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal

        int dim = 0;
        // a[dim1][dim2]
        String dim1 = "";
        String dim2 = "";
        ExpSymbol expsym;

        String str = "";
        String ident = "";

        ident = IdentMediate.analysis();

        while( getWordMed(poiMed).type == Token.LBRACK ){

            dim ++;
            poiMed++;
            expsym = ExpressionMediate.ConstExp();

            if( dim == 1 ){
                dim2 = expsym.token;
            }else{
                dim1 = expsym.token;
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){ // 具有初始赋值
            poiMed++;
            InitValMediate.analysis();
            if( dim == 0 ){
                str = "var int " + ident + " = " + InitValMediate.initValList[0]+ "\n";
                IntermediateCode.writeIntermediateCode(str);
            }else if( dim == 1 || dim == 2 ){
                str = "arr int " + ident + "[" + InitValMediate.numExp + "]"+ "\n"; // 由于赋值的特殊性，我们可以直接用该值表示数组的大小。
                IntermediateCode.writeIntermediateCode(str);
                for( int i = 0; i < InitValMediate.numExp; i++ ){
                    str = ident + "[" + i + "]" + " = " + InitValMediate.initValList[i]+ "\n";
                    IntermediateCode.writeIntermediateCode(str);
                }
            }
            InitValMediate.numExp = 0;
        }else{ // 无初始赋值
            if( dim == 0 ){
                str = "var int " + ident + "\n";
            }else if( dim == 1 ){
                str = "arr int " + ident + "[" + dim2 + "]"+ "\n";
            }else if( dim == 2 ){
                str = "arr int " + ident + "[" + dim1 + "]" + "[" + dim2 + "]"+ "\n";
            }
            IntermediateCode.writeIntermediateCode(str);
        }
    }
}
