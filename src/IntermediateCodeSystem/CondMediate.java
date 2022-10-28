package IntermediateCodeSystem;

import GrammaticalSystem.Expression;

import java.io.IOException;

import static IntermediateCodeSystem.ExpressionMediate.expStack;
import static IntermediateCodeSystem.ExpressionMediate.expStackTop;

public class CondMediate {

    public static String analysis( int type ) throws IOException {
        // Cond → LOrExp
        String reg = "";
        ExpAnalyse e = new ExpAnalyse();

        //TODO 先存假标签，再存真标签，保持在栈里的位置。
        LabelMediate nextLabel = LabelMediate.getFreeLabel(); // 后续的正常代码
        LabelMediate falseLabel = LabelMediate.getFreeLabel(); // else ... ; 循环体
        LabelMediate trueLabel = LabelMediate.getFreeLabel(); // if ... ; 循环头

        if( type == StmtMediate.WHILE ){
            String str = "br label " + trueLabel.reg;
            IntermediateCode.writeLlvmIr( str, true );
            LabelMediate.labelPrint();
            reg = trueLabel.reg;
            LoopMediate.addLoop( trueLabel.reg, nextLabel.reg );
        }

        ExpSymbol expsym = ExpressionMediate.LOrExp(e);

        if( type == StmtMediate.WHILE ){
            String str = "br i1 " + expsym.value + ", label " + falseLabel.reg + ", label " + nextLabel.reg;
            IntermediateCode.writeLlvmIr( str, true );
        }
        else if(  type == StmtMediate.IF  ){
            String str = "br i1 " + expsym.value + ", label " + trueLabel.reg + ", label " + falseLabel.reg;
            IntermediateCode.writeLlvmIr( str, true );
            reg = nextLabel.reg;
        }

        return reg;
    }
}
