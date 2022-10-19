package GrammaticalSystem;
import IntermediateCodeSystem.ExpAnalyse;
import LexicalSystem.Token;
import java.io.IOException;

import static GrammaticalSystem.Expression.expStack;
import static GrammaticalSystem.Expression.expStackTop;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Cond {

    public static void analysis() throws IOException {
        // Cond → LOrExp

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        Expression.LOrExp(e);

        expStackTop--;
        expStack[expStackTop].quaternion();


        writeGrammer("Cond");
    }

}
