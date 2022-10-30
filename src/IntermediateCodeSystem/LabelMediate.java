package IntermediateCodeSystem;

import java.io.IOException;

public class LabelMediate {

    public String reg; // %l1
    public String label; // l1

    public static LabelMediate[] labelStack = new LabelMediate[10000];
    public static int labelTop = 0;

    public static int poi = 0;

    public static LabelMediate getFreeLabel(){
        LabelMediate labmed = new LabelMediate();
        labelStack[labelTop++] = labmed;
        labmed.label = "l" + poi++;
        labmed.reg = "%" + labmed.label;
        return labmed;
    }

    public static void labelPrint() throws IOException {
        LabelMediate labmed = labelStack[--labelTop];
        IntermediateCode.writeLlvmIr("", false);
        IntermediateCode.writeLlvmIr( labmed.label + ":", false );

        //TODO 在每次添加新标签后都需要对当前全局变量进行重置。
        SymbolTableMediate.globalVarInit();
    }

}
