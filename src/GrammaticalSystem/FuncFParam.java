package GrammaticalSystem;
import ErrorHandlingSystem.NameRedefinitionError;
import ErrorHandlingSystem.WithOutBracket;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class FuncFParam {

    public static void Analysis() throws IOException {
        // FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        int type = 0;
        Lexical lex;
        BType();

        lex = getWord(poi);
        Ident.ident();

        if( getWord(poi).type == Token.LBRACK ){

            FuncDef.nowFuncParamList[FuncDef.nowFuncParamNum] ++; // 每有一次'['表示当前参数的维度加1

            type += 1;
            writeWord( getWord(poi) );
            poi++;

            if(  getWord(poi).type == Token.RBRACK ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutBracket.analyse(poi-1);
            }

            while( getWord(poi).type == Token.LBRACK ){

                FuncDef.nowFuncParamList[FuncDef.nowFuncParamNum] ++; // 每有一次'['表示当前参数的维度加1

                type += 1;
                writeWord( getWord(poi) );
                poi++;
                Expression.ConstExp();
                if( getWord(poi).type == Token.RBRACK ){
                    writeWord( getWord(poi) );
                    poi++;
                }else{
                    WithOutBracket.analyse(poi-1);
                }
            }
        }

        if( !NameRedefinitionError.analyse( lex ) ){
            // 如果当前的变量还未被声明，则将其添加至符号表中。
            Ident.identAnalyse( lex, type, 0 );
        }
        
        writeGrammer("FuncFParam");
    }

}
