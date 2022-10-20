package ErrorHandlingSystem;

import GrammaticalSystem.GrammaticalAnalysis;
import LexicalSystem.Lexical;
import SymbolTableSystem.Symbol;

import java.io.IOException;

public class NameUndefinitionError {

    public static boolean analyse(Lexical lex ) throws IOException {
        int dimension = GrammaticalAnalysis.nowDimension;
        while( dimension != -1 ){
            if( GrammaticalAnalysis.symbolTableList[dimension].directory.get(lex.token) == null ){
                // 如果在当前维度的单词表中未得到对应的声明，则寻找上一维度。
                dimension = GrammaticalAnalysis.symbolTableList[dimension].fatherId;
            }else{
                return false;
            }
        }
        ErrorHandling.writeError( lex.lineNumber, ErrorHandling.c );
        // 如果所有维度的单词表中均为找到声明，则报错。
        return true;
    }
}
