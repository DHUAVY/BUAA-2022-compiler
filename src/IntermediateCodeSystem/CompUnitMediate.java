package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class CompUnitMediate {
    public static void analysis() throws IOException {

        SymbolTableMediate.init();
        IOFuncDec();

        //TODO 定义变量时不对文件进行写入。
        IntermediateCode.writeInFile = false;
        while( (getWordMed(poiMed).type == Token.CONSTTK ||
                getWordMed(poiMed + 2 ).type != Token.LPARENT) &&
                (getWordMed(poiMed + 2 ).type != 0 ) ){
            // 0 -> const, 2 !-> (
            DeclMediate.analysis();
        }
        IntermediateCode.writeInFile = true;
        IntermediateCode.writeLlvmIr("", false); // 插入空行，方便阅读。

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
