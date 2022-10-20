package ErrorHandlingSystem;

import GrammaticalSystem.GrammaticalAnalysis;
import LexicalSystem.Lexical;

import java.io.IOException;

public class WithOutParenError { // 缺少小括号
    public static void analyse( int poi ) throws IOException {
        Lexical lex = GrammaticalAnalysis.getWord( poi );
        ErrorHandling.writeError( lex.lineNumber, ErrorHandling.j );
    }
}
