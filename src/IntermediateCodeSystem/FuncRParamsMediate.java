package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class FuncRParamsMediate {

    public static String analysis( FunctionMediate fun ) throws IOException {
        // FuncRParams → Exp { ',' Exp }
        int i = 0;
        String str = "";
        ExpSymbol expsym;

        expsym = ExpressionMediate.Exp();
        str += addFuncRParam( fun, i++, expsym );

        while(getWordMed(poiMed).type == Token.COMMA){
            str += ", ";
            poiMed++;
            expsym = ExpressionMediate.Exp();
            str += addFuncRParam( fun, i++, expsym );
        }
        return str;
    }

    //TODO 根据当前获得的是地址还是实值进行传参。
    public static String addFuncRParam( FunctionMediate fun, int poi, ExpSymbol expsym ) throws IOException {
        SymbolMediate param = fun.paramList[poi];
        int type = param.type;
        if( type == 0 ){
            return "i32 " + expsym.value;
        }
        else if( type == 1 ){
            return "i32* " + expsym.value;
        }
        else{
            int len = param.dim2;
            return "[" + len + " x i32]* " + expsym.value;
        }
    }
}
