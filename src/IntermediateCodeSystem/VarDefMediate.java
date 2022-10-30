package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.ExpAnalyse.*;
import static IntermediateCodeSystem.IntermediateCode.*;

public class VarDefMediate {

    public static void analysis() throws IOException {
        // VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
        int dim = 0;

        String dim1 = "0";
        String dim2 = "0";
        // a[dim1][dim2]

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
                dim1 = expsym.value; // a[dim1][dim2]
            }
            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        if( dim == 2 ){
            String tran = "0";
            tran = dim2;
            dim2 = dim1;
            dim1 = tran;
        }

        symmed.type = dim;
        symmed.dim1 = Integer.parseInt( dim1 );
        symmed.dim2 = Integer.parseInt( dim2 );
        // 维度信息。

        if( getWordMed(poiMed).type == Token.ASSIGN ){ // 具有初始赋值
            poiMed++;
            InitValMediate.analysis();

            if( dim == 0 ){
                if( nowMediateDimension != 0 ){

                    reg = TemporaryRegister.getFreeReg(); // 申请寄存器。
                    symmed.reg = reg; // 完善符号表。

                    str = reg + " = alloca i32";
                    IntermediateCode.writeLlvmIr( str, true );

                    if( InitValMediate.initValList[0].haveValue && mode == varMode ){
                        symmed.safe = true;
                        symmed.value = Integer.parseInt( InitValMediate.initValList[0].value );
                    }

                    str = "store i32 " + InitValMediate.initValList[0].value + ", i32* " + reg;
                    IntermediateCode.writeLlvmIr( str, true);
                }
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg; // 完善符号表。

                    if( InitValMediate.initValList[0].haveValue ){
                        symmed.safe = true;
                        symmed.value = Integer.parseInt( InitValMediate.initValList[0].value );
                    }

                    str = reg + " = global i32 " + InitValMediate.initValList[0].value;
                    IntermediateCode.writeGlobalVarDef( str );
                }

            }
            /*-----------------------一维数组并且具有初始赋值-----------------------*/
            else if( dim == 1 ){ //
                /*----------------------------一维数组,局部变量----------------------------*/
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    str = reg + " = alloca [" + dim2 + " x i32]";
                    IntermediateCode.writeLlvmIr( str, true);

                    //TODO 修改为 i32* 的格式。
                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeOneDimensionPatten( newReg, reg, dim2 );

                    //TODO 更新数组对应的寄存器。
                    reg = newReg;
                    symmed.reg = reg;
                    String arr = reg;

                    for( int i = 0;  i < InitValMediate.numExp; i++ ){

                        reg = TemporaryRegister.getFreeReg(); // 为数组的每一位置获取新的reg。
                        str = reg + IntermediateCode.getPoiOneDim( arr, String.valueOf(i) );
                        writeLlvmIr( str, true );

                        str = "store i32 " + InitValMediate.initValList[i].value + ", i32* " + reg;
                        writeLlvmIr( str, true);

                        if( InitValMediate.initValList[i].haveValue && mode == varMode ){
                            symmed.safeList[i] = true;
                            symmed.valueList[i] = Integer.parseInt( InitValMediate.initValList[0].value );
                        }
                    }
                }
                /*----------------------------一维数组,全局变量----------------------------*/
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;

                    str = reg + " = global [" + symmed.dim2 + " x i32] [";
                    for( int i = 0;  i < InitValMediate.numExp; i++ ){
                        str += "i32 " + InitValMediate.initValList[i].value;
                        if( i != InitValMediate.numExp - 1 ){
                            str += ", ";
                        }
                        if( InitValMediate.initValList[i].haveValue ){
                            symmed.safeList[i] = true;
                            symmed.valueList[i] = Integer.parseInt( InitValMediate.initValList[i].value );
                        }
                    }
                    str += "]";
                    IntermediateCode.writeGlobalVarDef( str );
                }

            }
            else if( dim == 2 ){ // 二维数组，具有初始赋值。
                /*----------------------------二维数组,局部变量----------------------------*/
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    str = reg + " = alloca [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]]";
                    IntermediateCode.writeLlvmIr( str, true);

                    //TODO 修改为 a x i32* 的格式。
                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeTwoDimensionPatten( newReg, reg, dim1, dim2 );

                    //TODO 更新数组对应的寄存器。
                    reg = newReg;
                    symmed.reg = reg;

                    //TODO 为数组赋值。
                    String arr = reg;
                    for( int i = 0, j = 0;  i < InitValMediate.numExp; i++ ){

                        reg = TemporaryRegister.getFreeReg(); // 为数组的每一位置获取新的reg。
                        str = reg + IntermediateCode.getPoiTwoDim(
                                arr,
                                String.valueOf(symmed.dim2),
                                String.valueOf(j),
                                String.valueOf(i-j*symmed.dim2)
                        );
                        writeLlvmIr( str, true );

                        str = "store i32 " + InitValMediate.initValList[i].value + ", i32* " + reg;
                        writeLlvmIr( str, true);
                        if( InitValMediate.initValList[i].haveValue && mode == varMode ){
                            symmed.safeList[i] = true;
                            symmed.valueList[i] = Integer.parseInt( InitValMediate.initValList[i].value );
                        }
                        j = (i + 1) / symmed.dim2;
                    }
                }
                /*----------------------------二维数组,全局变量----------------------------*/
                else{
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;

                    str = reg + " = global [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]] [";
                    for( int i = 0, j = 0;  i < InitValMediate.numExp; i++ ){
                        if( InitValMediate.initValList[i].haveValue ){
                            symmed.safeList[i] = true;
                            symmed.valueList[i] = Integer.parseInt( InitValMediate.initValList[i].value );
                        }
                        if( j == 0 ){
                            str += "[" + symmed.dim2 + " x i32] [";
                        }
                        str += "i32 " + InitValMediate.initValList[i].value;
                        if( j != symmed.dim2 - 1 ){
                            // 如果j不是当前组的最后一个。
                            str += ", ";
                        }
                        else if( j == symmed.dim2 - 1 && i != InitValMediate.numExp - 1){
                            // 如果j是当前组的最后一个，且当前组不是最后一组。
                            str += "], ";
                        }
                        j ++;
                        j = j % symmed.dim2;
                    }
                    str += "]]";
                    IntermediateCode.writeGlobalVarDef( str );
                }
            }
            InitValMediate.numExp = 0;
        }
        /*----------------------------无初始赋值----------------------------*/
        else{
            if( dim == 0 ){
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg(); // 申请寄存器。
                    symmed.reg = reg; // 完善符号表。

                    str = reg + " = alloca i32";
                    IntermediateCode.writeLlvmIr( str, true );
                }
                else{ // 如果是全局变量，则默认赋值为0。
                    reg = "@" + symmed.token; // 申请寄存器。

                    symmed.reg = reg;
                    symmed.safe = true;
                    symmed.value = 0; // 完善符号表。

                    str = reg + " = global i32 " + 0;
                    IntermediateCode.writeGlobalVarDef( str );
                }
            }
            /*----------------------------一维数组----------------------------*/
            else if( dim == 1 ){
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    symmed.reg = reg;
                    str = reg + " = alloca [" + dim2 + " x i32]";
                    IntermediateCode.writeLlvmIr( str, true);

                    //TODO 修改为 i32* 的格式。
                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeOneDimensionPatten( newReg, reg, dim2 );

                    //TODO 更新数组对应的寄存器。
                    reg = newReg;
                    symmed.reg = reg;
                }
                else{ // 一维数组，全局变量。
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;
                    str = reg + " = global [" + symmed.dim2 + " x i32] zeroinitializer";

                    IntermediateCode.writeGlobalVarDef( str );
                    for( int i = 0; i < symmed.dim2; i++ ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = 0;
                    }
                }
            }
            /*----------------------------二维数组----------------------------*/
            else if( dim == 2 ){
                if( nowMediateDimension != 0 ){
                    reg = TemporaryRegister.getFreeReg();
                    symmed.reg = reg;
                    str = reg + " = alloca [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]]";
                    IntermediateCode.writeLlvmIr( str, true);

                    //TODO 修改为 a x i32* 的格式。
                    String newReg = TemporaryRegister.getFreeReg(); // newreg
                    IntermediateCode.changeTwoDimensionPatten( newReg, reg, dim1, dim2 );

                    //TODO 更新数组对应的寄存器。
                    reg = newReg;
                    symmed.reg = reg;
                }
                else{ // 二维数组，全局变量。
                    reg = "@" + symmed.token; // 申请寄存器。
                    symmed.reg = reg;
                    str = reg + " = global [" + symmed.dim1 + " x [" + symmed.dim2 + " x i32]] zeroinitializer";

                    IntermediateCode.writeGlobalVarDef( str );
                    for( int i = 0; i < symmed.dim1 * symmed.dim2; i++ ){
                        symmed.safeList[i] = true;
                        symmed.valueList[i] = 0;
                    }
                }
            }
        }
    }
}
