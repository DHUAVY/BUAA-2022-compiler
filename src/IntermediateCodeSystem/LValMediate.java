package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class LValMediate {

    public static lvalSym analysis() throws IOException {
        // LVal → Ident {'[' Exp ']'}
        int dim = 0;
        String poi = "";
        String token = "";
        String newReg = "";

        ExpSymbol expsym1 = new ExpSymbol("", 1, false);
        ExpSymbol expsym2 = new ExpSymbol("", 1, false);

        if( getWordMed(poiMed).type == Token.IDENFR ){
            token = getWordMed(poiMed).token; // 标识。
            poiMed++;
        }
        while( getWordMed(poiMed).type == Token.LBRACK ){

            dim ++;
            poiMed++;
            if( dim == 1 ){
                expsym2 = ExpressionMediate.Exp();
            }
            else if( dim == 2 ){
                expsym1 = ExpressionMediate.Exp();
            }

            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
            }
        }

        if( dim == 0 ){
            return new lvalSym( token, expsym2.token, expsym2.haveValue );
        }
        else if( dim == 1 ){
            return new lvalSym( token, expsym2.token, expsym2.haveValue );
        }
        else if( dim == 2 ){
            if( )
        }

        return new lvalSym( token, dim, poi, dim );
    }
}

class lvalSym{
    int dim; // 其中含有左括号的数量，也即选取的维度。
    String token;
    String poi;
    boolean haveValue;

    public lvalSym( String token, String poi, boolean haveValue, int dim ){
        this.dim = dim;
        this.poi = poi;
        this.token = token;
        this.haveValue = haveValue;
    }
}

