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
        String dim1 = "0";
        String dim2 = "0";
        String reg = ""; // 该变量对应的寄存器。
        ExpSymbol expsym;

        String str = "";
        String ident = "";

        ident = IdentMediate.analysis();

        SymbolMediate symmed = IntermediateCode.symbolTableMediateList[IntermediateCode.nowMediateDimension].addSymbol( ident );
        symmed.token = ident;
        symmed.con = false;
        symmed.id = poiMed - 1;
        symmed.dimension = IntermediateCode.nowMediateDimension;
        // 基础信息。

        while( getWordMed(poiMed).type == Token.LBRACK ){

            dim ++;
            poiMed++;
            expsym = ExpressionMediate.ConstExp();

            if( dim == 1 ){
                dim2 = expsym.value;
            }else{
                dim1 = expsym.value;
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        symmed.type = dim;
        symmed.dim1 = Integer.parseInt( dim1 );
        symmed.dim2 = Integer.parseInt( dim2 );
        // 维度信息。

        if( getWordMed(poiMed).type == Token.ASSIGN ){ // 具有初始赋值
            poiMed++;
            InitValMediate.analysis();
            if( dim == 0 ){

                reg = TemporaryRegister.getFreeReg(); // 申请寄存器。
                str = reg + " = alloca i32"; // 完善符号表。
                IntermediateCode.writeLlvmIr( str, true );


                if( InitValMediate.initValList[0].haveValue ){
                    symmed.safe = true;
                    symmed.value = Integer.parseInt( InitValMediate.initValList[0].value );
                }
                str = "store i32 " + InitValMediate.initValList[0].value + ", i32* " + reg;
                IntermediateCode.writeLlvmIr( str, true);

            }
            else if( dim == 1 || dim == 2 ){
                str = "arr int " + ident + "[" + InitValMediate.numExp + "]"; // 由于赋值的特殊性，我们可以直接用该值表示数组的大小。
                IntermediateCode.writeIntermediateCode(str);

                for( int i = 0; i < InitValMediate.numExp; i++ ){
                    str = ident + "[" + i + "]" + " = " + InitValMediate.initValList[i].value;

                    if( InitValMediate.initValList[i].haveValue ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = Integer.parseInt( InitValMediate.initValList[0].value );
                    }

                    IntermediateCode.writeIntermediateCode(str);
                }

            }
            InitValMediate.numExp = 0;
        }
        else{ // 无初始赋值
            if( dim == 0 ){
                reg = TemporaryRegister.getFreeReg(); // 申请寄存器。
                symmed.reg = reg; // 完善符号表。

                str = reg + " = alloca i32";
                IntermediateCode.writeLlvmIr( str, true );
                if( IntermediateCode.nowMediateDimension == 0 ){ // 如果是全局变量，则默认赋值为0。
                    symmed.safe = true;
                    symmed.value = 0;
                }
            }
            else if( dim == 1 ){
                str = "arr int " + ident + "[" + dim2 + "]";
                if( IntermediateCode.nowMediateDimension == 0 ){ // 如果是全局变量，则默认赋值为0。
                    for( int i = 0; i < symmed.dim2; i++ ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = 0;
                    }
                }
            }
            else if( dim == 2 ){
                str = "arr int " + ident + "[" + dim1 + "]" + "[" + dim2 + "]";
                if( IntermediateCode.nowMediateDimension == 0 ){ // 如果是全局变量，则默认赋值为0。
                    for( int i = 0; i < symmed.dim2 * symmed.dim2; i++ ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = 0;
                    }
                }
            }
            IntermediateCode.writeIntermediateCode(str);
        }
        System.out.println( symmed );
    }
}
