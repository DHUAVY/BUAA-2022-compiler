package GrammaticalSystem;
import ErrorHandlingSystem.*;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import SymbolTableSystem.Symbol;
import SymbolTableSystem.SymbolTable;

import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class LVal {

    public static int Analysis( int judge ) throws IOException {
        // LVal → Ident {'[' Exp ']'}
        Symbol sym = null;
        int ret = 0;
        int dimension = 0;
        boolean flag; // 判断是否需要进一步判断类型错误。

        Lexical lex = getWord(poi);

        flag = NameUndefinitionError.analyse( lex ); // true -> error

        if( !flag ){ ; // 如果当前的Ident在单词表中被查到，之后再去查看类型是否匹配。
            sym = lval(lex);
        }

        if( judge == 1 && !flag ){ // 赋值语句
            if( sym.con == 1 ){
                ConstChangeError.analyse( lex.lineNumber );
            }
        }

        Ident.ident();
        while( getWord(poi).type == Token.LBRACK ){

            dimension ++; // 每多一层'['，代表当前变量的维度减1。

            writeWord( getWord(poi) );
            poi++;
            Expression.Exp();

            if( getWord(poi).type == Token.RBRACK ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutBracket.analyse(poi-1);
            }
        }

        writeGrammer("LVal");
        if( !flag ){
            return (sym.type - dimension);
        }else{
            return -1;
        }

    }

    public static Symbol lval(Lexical lex ) throws IOException {
        Symbol sym = null;
        int dimension = GrammaticalAnalysis.nowDimension;
        while( dimension != -1 ){
            sym = GrammaticalAnalysis.symbolTableList[dimension].directory.get(lex.token);
            if( sym == null ){
                // 如果在当前维度的单词表中未得到对应的声明，则寻找上一维度。
                dimension = GrammaticalAnalysis.symbolTableList[dimension].fatherId;
                continue;
            }
            return sym;
        }
        return sym;
    }

}
