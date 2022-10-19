package ErrorHandlingSystem;

import LexicalSystem.Lexical;

import java.io.IOException;

public class ConstChangeError {

    public static void analyse( int lineNumber ) throws IOException {
        ErrorHandling.writeError( lineNumber, ErrorHandling.h );
    }

}
