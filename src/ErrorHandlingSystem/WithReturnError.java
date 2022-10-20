package ErrorHandlingSystem;

import LexicalSystem.Lexical;

import java.io.IOException;

public class WithReturnError {

    public static int lineNumber;

    public static void analyse() throws IOException {
        ErrorHandling.writeError( lineNumber, ErrorHandling.f );
        lineNumber = 0;
    }

}
