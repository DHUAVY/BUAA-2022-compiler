package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.ExpAnalyse.*;
import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;
import static IntermediateCodeSystem.StringMediate.*;

public class CompUnitMediate {
    public static void analysis() throws IOException {

        /*初始化符号表*/
        SymbolTableMediate.init();

        /*在定义全局变量时，一定可以求出全部的值。*/
        mode = varMode;
        /*定义变量时，其中的运算表达式，不对文件进行写入。*/
        IntermediateCode.writeInFile = false;
        while( (getWordMed(poiMed).type == Token.CONSTTK ||
                getWordMed(poiMed + 2 ).type != Token.LPARENT) &&
                (getWordMed(poiMed + 2 ).type != 0 ) ){
            // 0 -> const, 2 !-> (
            DeclMediate.analysis();
        }
        IntermediateCode.writeInFile = true;
        IntermediateCode.writeLlvmIr("", false); // 插入空行，方便阅读。

        mode = funcMode;
        while( getWordMed( poiMed + 1).type != Token.MAINTK && getWordMed( poiMed + 1).type != 0 ){
            FuncDefMediate.analysis();
            SymbolTableMediate.globalVarInit();
            TemporaryRegister.poi = 0;
        }

        TemporaryRegister.poi = 1;
        MainFuncDefMediate.analysis();
        SymbolTableMediate.globalVarInit();
    }

    public static void IOFuncDec() throws IOException {
        String func = "declare i32 @getint()";
        IntermediateCode.writeLlvmIr( func, false );
        func = "declare void @putint(i32)";
        IntermediateCode.writeLlvmIr( func, false );
        func = "declare void @putch(i32)";
        IntermediateCode.writeLlvmIr( func, false );
        func = "declare void @putstr(i8*)";
        IntermediateCode.writeLlvmIr( func, false );

        IntermediateCode.writeLlvmIr( "", false ); // 输出空行
    }
}
