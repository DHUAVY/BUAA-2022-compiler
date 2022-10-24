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
        str += addFuncRParam( fun.paramList[i++], expsym );

        while(getWordMed(poiMed).type == Token.COMMA){
            str += ", ";
            poiMed++;
            expsym = ExpressionMediate.Exp();
            str += addFuncRParam( fun.paramList[i++], expsym );
        }
        return str;
    }

    //TODO 根据当前获得的是地址还是实值进行传参。
    public static String addFuncRParam( int type, ExpSymbol expsym ) throws IOException {
        if( type == 0 ){
            return "i32 " + expsym.value;
        }
        else{
            return "i32* " + expsym.value;
        }
    }
}
