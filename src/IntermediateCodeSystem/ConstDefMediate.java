package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;
import static IntermediateCodeSystem.SymbolTableMediate.*;

public class ConstDefMediate {
    public static void ConstDefAnalysis() throws IOException {
        // ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal

        SymbolMediate symmed;
        String ident = "";
        String reg = "";

        int dim = 0;
        int dim1 = 0;
        int dim2 = 0;
        // a[dim1][dim2]

        ident = IdentMediate.analysis();

        symmed = IntermediateCode.symbolTableMediateList[IntermediateCode.nowMediateDimension].addSymbol( ident );
        symmed.con = true;
        symmed.safe = true;
        symmed.id = poiMed - 1;
        symmed.dimension = IntermediateCode.nowMediateDimension;
        symmed.token = ident;
        // 在符号表中添加符号并完善信息。

        while( getWordMed(poiMed).type == Token.LBRACK ){ // {
            dim++;
            poiMed++;
            String str = ExpressionMediate.ConstExp().value;

            if( dim == 1 ){
                dim2 = Integer.parseInt( str );
            }else if( dim == 2 ){
                dim1 = Integer.parseInt( str );
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){ // }
                poiMed++;
            }
        }

        if( dim == 2 ){
            int tran = 0;
            tran = dim2;
            dim2 = dim1;
            dim1 = tran;
        }

        symmed.type = dim;
        symmed.dim2 = dim2;
        symmed.dim1 = dim1;
        // 维度信息

        if( getWordMed(poiMed).type == Token.ASSIGN ){

            poiMed++;
            ConstInitValMediate.analyse();

            if( dim == 0 ){ // 普通变量。
                int value = Integer.parseInt( ConstInitValMediate.initValList[0] );
                symmed.value = value;
                // 完善符号表。
                /*----------------------------局部变量----------------------------*/
                if( symmed.dimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    symmed.reg = reg;

                    String str = reg + " = alloca i32";
                    IntermediateCode.writeLlvmIr( str, true);
                    str = "store i32 " + value + ", i32* " + reg;
                    IntermediateCode.writeLlvmIr( str, true);
                }
                /*----------------------------全局变量----------------------------*/
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg; // 完善符号表。

                    String str = reg + " = global i32 " + value;
                    IntermediateCode.writeGlobalVarDef( str );
                }
            }
            /*----------------------------一维数组----------------------------*/
            else if( dim == 1 ) {
                /*----------------------------一维数组,局部变量----------------------------*/
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    String str = reg + " = alloca [" + dim2 + " x i32]";
                    IntermediateCode.writeLlvmIr( str, true);

                    //TODO 修改为 i32* 的格式。
                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeOneDimensionPatten( newReg, reg, String.valueOf(dim2) );

                    //TODO 更新数组对应的寄存器。
                    reg = newReg;
                    symmed.reg = reg;
                    String arr = reg;

                    for( int i = 0;  i < ConstInitValMediate.numExp; i++ ){

                        reg = TemporaryRegister.getFreeReg(); // 为数组的每一位置获取新的reg。
                        str = reg + IntermediateCode.getPoiOneDim( arr, String.valueOf(i));
                        writeLlvmIr( str, true );

                        str = "store i32 " + ConstInitValMediate.initValList[i] + ", i32* " + reg;
                        writeLlvmIr( str, true);

                        symmed.safeList[i] = true;
                        symmed.valueList[i] = Integer.parseInt( ConstInitValMediate.initValList[i] );
                    }
                }
                /*----------------------------一维数组,全局变量----------------------------*/
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;

                    StringBuilder str = new StringBuilder(reg + " = constant [" + symmed.dim2 + " x i32] [");

                    for( int i = 0;  i < ConstInitValMediate.numExp; i++ ){
                        str.append("i32 ");
                        str.append(ConstInitValMediate.initValList[i]);

                        if( i != ConstInitValMediate.numExp - 1 ){
                            str.append(", ");
                        }

                        symmed.safeList[i] = true;
                        symmed.valueList[i] = Integer.parseInt( ConstInitValMediate.initValList[i] );
                    }
                    str.append("]");
                    IntermediateCode.writeGlobalVarDef( str.toString() );
                }
            }
            /*----------------------------二维数组----------------------------*/
            else if( dim == 2 ){
                /*----------------------------二维数组,局部变量----------------------------*/
                if( nowMediateDimension != 0 ){

                    reg = TemporaryRegister.getFreeReg();
                    String str = reg + " = alloca [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]]";
                    IntermediateCode.writeLlvmIr( str, true);

                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeTwoDimensionPatten( newReg, reg, String.valueOf(dim1), String.valueOf(dim2) );

                    reg = newReg;
                    symmed.reg = reg;
                    String arr = reg;

                    for( int i = 0, j = 0;  i < ConstInitValMediate.numExp; i++ ){

                        reg = TemporaryRegister.getFreeReg(); // 为数组的每一位置获取新的reg。
                        str = reg + IntermediateCode.getPoiTwoDim(
                                arr,
                                String.valueOf(symmed.dim2),
                                String.valueOf(j),
                                String.valueOf(i-j*symmed.dim2)
                        );
                        writeLlvmIr( str, true );

                        str = "store i32 " + ConstInitValMediate.initValList[i] + ", i32* " + reg;
                        IntermediateCode.writeLlvmIr( str, true );

                        symmed.safeList[i] = true;
                        symmed.valueList[i] = Integer.parseInt( ConstInitValMediate.initValList[i] );
                        j = (i + 1) / symmed.dim2;
                    }

                }
                /*----------------------------二维数组,全局变量----------------------------*/
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;

                    StringBuilder str = new StringBuilder(reg + " = constant [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]] [");
                    for( int i = 0, j = 0;  i < ConstInitValMediate.numExp; i++ ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = Integer.parseInt( ConstInitValMediate.initValList[i]);
                        if( j == 0 ){
                            str.append("[");
                            str.append(symmed.dim2);
                            str.append(" x i32] [");
                        }
                        str.append("i32 ");
                        str.append(ConstInitValMediate.initValList[i]);
                        if( j != symmed.dim2 - 1 ){
                            // 如果j不是当前组的最后一个。
                            str.append(", ");
                        }
                        else if( j == symmed.dim2 - 1 && i != ConstInitValMediate.numExp - 1){
                            // 如果j是当前组的最后一个，且当前组不是最后一组。
                            str.append("], ");
                        }
                        j ++;
                        j = j % symmed.dim2;
                        j = j % symmed.dim2;
                    }
                    str.append("]]");
                    IntermediateCode.writeGlobalVarDef( str.toString() );
                }
            }
            ConstInitValMediate.numExp = 0;
        }
    }
}
