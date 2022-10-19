package ErrorHandlingSystem;

import java.io.IOException;

public class WithOutReturnError {

    public static int lineNumber;

    public static void analyse() throws IOException {
        ErrorHandling.writeError( lineNumber, ErrorHandling.g );
        lineNumber = 0;
    }
}
