package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class LValMediate {

    public static LValSym analysis() throws IOException {
        // LVal → Ident {'[' Exp ']'}
        int dim = 0;
        String poi = "";
        String token = "";
        String newReg = "";
        ExpSymbol expsym1 = new ExpSymbol("", 1);
        ExpSymbol expsym2 = new ExpSymbol("", 1);

        if( getWordMed(poiMed).type == Token.IDENFR ){
            token = getWordMed(poiMed).token; // 标识。
            poiMed++;
        }
        while( getWordMed(poiMed).type == Token.LBRACK ){

            dim ++;

            poiMed++;
            if( dim == 1 ){
                expsym1 = ExpressionMediate.Exp();
            }else if( dim == 2 ){
                expsym2 = ExpressionMediate.Exp();
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        if( dim == 1 ){
            poi = expsym1.token;
        }else if( dim == 2 ){
            newReg = TemporaryRegister.getFreeReg();
            String str = newReg + " = " + "";
        }
        return new LValSym( token, dim, poi );
    }
}

class LValSym{
    String token;
    int dim; // 当前的Lval是否为数组。
    String poi; // 如果当前的Lval为数组，则其访问的位置。

    public LValSym( String token, int dim, String poi ){
        this.dim = dim;
        this.poi = poi;
        this.token = token;
    }
}
