package GrammaticalSystem;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class FuncFParams {

    public static void Analysis() throws IOException {

        // FuncFParams → FuncFParam { ',' FuncFParam }

        FuncFParam.Analysis();
        FuncDef.nowFuncParamNum ++; // 每有一个变量声明，当前函数的参数数量加1。

        while( getWord(poi).type == Token.COMMA ){
            writeWord( getWord(poi) );
            poi++;
            FuncFParam.Analysis();
            FuncDef.nowFuncParamNum ++; // 每有一个变量声明，当前函数的参数数量加1。
        }

        writeGrammer("FuncFParams");
    }
}
