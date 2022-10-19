package IntermediateCodeSystem;

import GrammaticalSystem.Expression;

import java.io.IOException;

public class CondMediate {

    public static void analysis() throws IOException {
        // Cond → LOrExp

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        Expression.LOrExp(e);

        expStackTop--;
        expStack[expStackTop].quaternion();

    }
}
