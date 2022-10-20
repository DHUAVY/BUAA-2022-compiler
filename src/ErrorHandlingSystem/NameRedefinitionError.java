package ErrorHandlingSystem;

import GrammaticalSystem.GrammaticalAnalysis;
import LexicalSystem.Lexical;

import java.io.IOException;

public class NameRedefinitionError {

    public static boolean analyse( Lexical lex ) throws IOException {
        if( GrammaticalAnalysis.symbolTableList[GrammaticalAnalysis.nowDimension].directory.get(lex.token) != null ){
            // 如果在当前维度的单词表中得到了对应的声明，则表示出现了问题。
            ErrorHandling.writeError( lex.lineNumber, ErrorHandling.b );
            return true;
        }else{
            return false;
        }
    }
}
