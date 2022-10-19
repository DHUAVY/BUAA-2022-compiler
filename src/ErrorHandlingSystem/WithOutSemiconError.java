package ErrorHandlingSystem;

import GrammaticalSystem.GrammaticalAnalysis;
import LexicalSystem.Lexical;

import java.io.IOException;

public class WithOutSemiconError { // 缺少分号
    public static void analyse( int poi ) throws IOException {
        Lexical lex = GrammaticalAnalysis.getWord( poi );
        ErrorHandling.writeError( lex.lineNumber, ErrorHandling.i );
    }
}
