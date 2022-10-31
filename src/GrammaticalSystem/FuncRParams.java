package GrammaticalSystem;
import ErrorHandlingSystem.FuncParamNumError;
import ErrorHandlingSystem.FuncParamTypeError;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class FuncRParams {

    public static int funcNum = 0;
    public static Lexical[] nowFunc = new Lexical[5000]; // 存储当前正在处理的函数表，防止递归调用。

    public static void Analysis( Lexical lex, boolean flag ) throws IOException {
        // FuncRParams → Exp { ',' Exp }
        // flag为true表示当前分析的函数未定义。
        int type = 0;
        int nowFuncParamNum = 0;

        FuncRParams.nowFunc[funcNum] = lex;
        funcNum++; // 新函数入栈。

        type = Expression.Exp();

        if( !flag ){
            FuncParamTypeError.analyse( type, nowFuncParamNum );
        }

        nowFuncParamNum ++;

        while(getWord(poi).type == Token.COMMA){
            //writeWord( getWord(poi) );
            poi++;

            type = Expression.Exp();
            if( !flag ){
                FuncParamTypeError.analyse( type, nowFuncParamNum);
            }
            nowFuncParamNum ++;

        }

        if( !flag){
            FuncParamNumError.analyse( lex, nowFuncParamNum );
        }

        funcNum--;

//        writeGrammer("FuncRParams");
    }

}
