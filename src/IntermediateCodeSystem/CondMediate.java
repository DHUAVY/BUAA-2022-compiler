package IntermediateCodeSystem;

import GrammaticalSystem.Expression;

import java.io.IOException;

import static IntermediateCodeSystem.ExpressionMediate.expStack;
import static IntermediateCodeSystem.ExpressionMediate.expStackTop;

public class CondMediate {

    public static void analysis() throws IOException {
        // Cond → LOrExp

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        ExpressionMediate.LOrExp(e);

        expStackTop--;
        expStack[expStackTop].quaternion();

    }
}
