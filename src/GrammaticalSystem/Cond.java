package GrammaticalSystem;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Cond {

    public static void analysis() throws IOException {
        // Cond → LOrExp
        Expression.LOrExp();
        writeGrammer("Cond");
    }

}
