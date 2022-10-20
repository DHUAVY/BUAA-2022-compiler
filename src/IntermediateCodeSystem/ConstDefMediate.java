package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;
import static IntermediateCodeSystem.SymbolTableMediate.*;

public class ConstDefMediate {
    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

        SymbolMediate symmed;
        String str = "";
        String ident = "";

        int dim = 0;
        int dim1 = 0;
        int dim2 = 0;
        // a[dim1][dim2]

        ident = IdentMediate.analysis();

        symmed = symbolTableMediateList[nowMediateDimension].addSymbol( ident );
        symmed.con = true;
        symmed.safe = true;
        symmed.id = poiMed - 1;
        symmed.dimension = nowMediateDimension;
        // 在符号表中添加符号并完善信息。

        while( getWordMed(poiMed).type == Token.LBRACK ){ // {
            dim++;
            poiMed++;
            str = ExpressionMediate.ConstExp().token;

            if( dim == 1 ){
                dim2 = Integer.parseInt( str );
            }else if( dim == 2 ){
                dim1 = Integer.parseInt( str );
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){ // }
                poiMed++;
            }
        }

        if( getWordMed(poiMed).type == Token.ASSIGN ){

            poiMed++;
            ConstInitValMediate.analyse();

            if( dim == 0 ){
                int value = Integer.parseInt( ConstInitValMediate.initValList[0] );

                symmed.type = 0;
                symmed.value = value;
                // 完善符号表。

                str = "var int " + ident + " = " + value + "\n";
                IntermediateCode.writeIntermediateCode(str);
            }
            else if( dim == 1 ) {

                symmed.type = 1;
                symmed.dim2 = dim2;
                // 完善符号表。

                str = "arr int " + ident + "[" + dim2 + "]"+ "\n"; // 由于赋值的特殊性，我们可以直接用该值表示数组的大小。
                IntermediateCode.writeIntermediateCode(str);

                for( int i = 0; i < ConstInitValMediate.numExp; i++ ){

                    symmed.safeList[i] = true;
                    symmed.valueList[i] = Integer.parseInt(ConstInitValMediate.initValList[i]);
                    // 完善符号表。

                    str = ident + "[" + i + "]" + " = " + ConstInitValMediate.initValList[i]+ "\n";
                    IntermediateCode.writeIntermediateCode(str);
                }
            }
            else if( dim == 2 ){

                symmed.type = 2;
                symmed.dim2 = dim2;
                symmed.dim1 = dim1;
                // 完善符号表。

                str = "arr int " + ident + "[" + dim1 + "]" + "[" + dim2 + "]" + "\n"; // 由于赋值的特殊性，我们可以直接用该值表示数组的大小。
                IntermediateCode.writeIntermediateCode(str);

                for( int i = 0; i < ConstInitValMediate.numExp; i++ ){

                    symmed.safeList[i] = true;
                    symmed.valueList[i] = Integer.parseInt(ConstInitValMediate.initValList[i]);
                    // 完善符号表。

                    str = ident + "[" + i + "]" + " = " + ConstInitValMediate.initValList[i]+ "\n";
                    IntermediateCode.writeIntermediateCode(str);
                }
            }

            ConstInitValMediate.numExp = 0;
        }
    }
}
