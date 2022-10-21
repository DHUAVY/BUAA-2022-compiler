package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;

public class LValMediate {

    public static lvalSym analysis() throws IOException {
        // LVal → Ident {'[' Exp ']'}
        int dim = 0;
        String token = "";

        ExpSymbol expsym1 = new ExpSymbol("", 1, false);
        ExpSymbol expsym2 = new ExpSymbol("0", 1, false);

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

        String poi = expsym2.value;
        boolean haveValue = expsym2.haveValue;

        if( dim == 2 ){
            int dimLength = SymbolTableMediate.findSymbol(token).dim2;
            if( expsym1.haveValue && expsym2.haveValue ){
                int ret = Integer.parseInt( expsym1.value ) * dimLength;
                ret += Integer.parseInt( expsym2.value );

                poi = String.valueOf( ret );
                haveValue = true;
            }
            else if(  expsym1.haveValue ){
                int ret = Integer.parseInt( expsym1.value ) * dimLength;
                String reg = TemporaryRegister.getFreeReg();
                poi = reg;

                String str = reg + " = " + ret + " + " + expsym2.value;
                IntermediateCode.writeIntermediateCode( str );

                haveValue = false;
            }
            else{
                // a[a][b]  a[2][2]
                String reg = TemporaryRegister.getFreeReg(); // $t1
                String str = reg + " = " + dimLength + " * " + expsym1.value; // $t1 = 2 * a
                poi = reg; // poi = $t1
                IntermediateCode.writeIntermediateCode( str );

                reg = TemporaryRegister.getFreeReg(); // $t2
                str = reg + " = " + poi + " + " + expsym2.value; // $t2 = $t1 + b
                poi = reg; // poi = $t2
                IntermediateCode.writeIntermediateCode( str );

                haveValue = false;
            }
        }

        return new lvalSym( token, poi, haveValue, dim);

    }
}

