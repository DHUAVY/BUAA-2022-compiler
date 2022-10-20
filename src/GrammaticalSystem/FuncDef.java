package GrammaticalSystem;
import ErrorHandlingSystem.NameRedefinitionError;
import ErrorHandlingSystem.WithOutParenError;
import ErrorHandlingSystem.WithOutReturnError;
import ErrorHandlingSystem.WithReturnError;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import SymbolTableSystem.Function;
import SymbolTableSystem.FunctionTable;
import SymbolTableSystem.Symbol;
import SymbolTableSystem.SymbolTable;

import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class FuncDef {

    public static int nowFuncParamNum = 0; // 当前正在定义的函数的参数数量。
    public static int[] nowFuncParamList = new int[50000]; // 当前正在定义的函数的参数表。
    public static void FuncDefAnalysis() throws IOException {

        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        int type;
        int returnValue = -1; // 0 -> none, 1 -> int
        boolean flag;
        Lexical lex;

        type = FuncType();

        lex = getWord(poi);

        flag = NameRedefinitionError.analyse( lex ); // flag:true -> error
        Ident.identAnalyse(lex, -1, type); // 添加到函数表中，并且添加至当前维度的符号表中。

        if( !flag ){ // 如果未重复命名。

            symbolTableList[dimensionNum] = new SymbolTable( dimensionNum, nowDimension );
            nowDimension = dimensionNum;
            dimensionNum++;
            // 先判定全局变量是否有与其发生冲突，再添加新的维度。

        }

        Ident.ident();

        if( getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.RPARENT ){
                writeWord( getWord(poi) );
                poi++;

                if( !flag ){
                    completeFuncInfo( lex ); // 在block分析之前进行完善信息，防止递归情况的出现。
                }

                returnValue = Block.analysis( false );
                // 对应的函数下的block就无需再添加新的维度。
            }
            else if( getWord(poi).type == Token.LBRACE ){ // 没有参数，并且缺少右小括号。
                WithOutParenError.analyse( poi - 1);

                if( !flag ){
                    completeFuncInfo( lex ); // 在block分析之前进行完善信息，防止递归情况的出现。
                }

                returnValue = Block.analysis( false );
                //  对应的函数下的block就无需再添加新的维度。
            }
            else{
                FuncFParams.Analysis();
                if( getWord(poi).type == Token.RPARENT ){
                    writeWord( getWord(poi) );
                    poi++;

                    if( !flag ){
                        completeFuncInfo( lex ); // 在block分析之前进行完善信息，防止递归情况的出现。
                    }

                    returnValue = Block.analysis( false );
                    //  对应的函数下的block就无需再添加新的维度。
                }else{ // 有参数，并且缺少右小括号。
                    WithOutParenError.analyse( poi - 1);

                    if( !flag ){
                        completeFuncInfo( lex ); // 在block分析之前进行完善信息，防止递归情况的出现。
                    }

                    returnValue = Block.analysis( false );
                    //  对应的函数下的block就无需再添加新的维度。
                }
            }
        }else{
            wrong();
        }

        if( type == 0 && returnValue == 1 && !flag ){
            WithReturnError.analyse();
        }
        else if( type == 1 && returnValue == 0 && !flag ){
            WithOutReturnError.analyse();
        }

        writeGrammer("FuncDef");
    }

    public static void completeFuncInfo( Lexical lex ){ // 复制当前的函数的参数信息。

        Function func = FunctionTable.directory.get( lex.token );
        func.paramNum = nowFuncParamNum;

        for( int i = 0; i < nowFuncParamNum; i++ ){
            func.paramList[i] = nowFuncParamList[i];
            nowFuncParamList[i] = 0;
        }

        nowFuncParamNum = 0;

    }
}
