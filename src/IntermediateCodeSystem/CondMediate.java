package IntermediateCodeSystem;

import GrammaticalSystem.Expression;

import java.io.IOException;

import static IntermediateCodeSystem.ExpressionMediate.expStack;
import static IntermediateCodeSystem.ExpressionMediate.expStackTop;

public class CondMediate {

    public static void analysis() throws IOException {
        // Cond → LOrExp
        ExpAnalyse e = new ExpAnalyse();

        ExpSymbol expsym = ExpressionMediate.LOrExp(e);

        //TODO 先存假标签，再存真标签，保持在栈里的位置。
        LabelMediate falseLabel = LabelMediate.getFreeLabel();
        LabelMediate trueLabel = LabelMediate.getFreeLabel();

        String str = "br i1 " + expsym.value + ", label " + trueLabel.reg + ", label " + falseLabel.reg;
        IntermediateCode.writeLlvmIr( str, true );
    }
}
