package ErrorHandlingSystem;

import LexicalSystem.Lexical;
import SymbolTableSystem.Function;
import SymbolTableSystem.FunctionTable;

import java.io.IOException;

public class FuncParamNumError {
    public static void analyse( Lexical lex, int paramNum ) throws IOException {
        Function fun = FunctionTable.directory.get( lex.token );
        if( fun.paramNum != paramNum ){
            ErrorHandling.writeError( lex.lineNumber, ErrorHandling.d );
        }
    }
}
