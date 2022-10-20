package ErrorHandlingSystem;

import GrammaticalSystem.FuncRParams;
import LexicalSystem.Lexical;
import SymbolTableSystem.Function;
import SymbolTableSystem.FunctionTable;
import SymbolTableSystem.Symbol;

import java.io.IOException;

public class FuncParamTypeError {
    public static void analyse( int type, int poi ) throws IOException {
        Lexical lex = FuncRParams.nowFunc[FuncRParams.funcNum-1];
        Function func = FunctionTable.directory.get( lex.token );
        if( func.paramList[poi] != type ){
            ErrorHandling.writeError( lex.lineNumber, ErrorHandling.e );
        }
    }
}
