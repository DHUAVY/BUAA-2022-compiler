package ErrorHandlingSystem;

import java.io.IOException;

public class FormatCharacterError {
    public static void analyse( int lineNum ) throws IOException {
        ErrorHandling.writeError( lineNum, ErrorHandling.l );
    }
}
