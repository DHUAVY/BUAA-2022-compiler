package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;

public class FuncFParamMediate {

    public static void analysis( FunctionMediate func ) throws IOException {
        // FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
        int id = 0;
        int dim = 0;
        String ident;
        ExpSymbol expsym = new ExpSymbol( "0", 1, false);
        SymbolMediate symmed;

        BTypeMed();

        id = poiMed;
        ident = IdentMediate.analysis();

        if( getWordMed(poiMed).type == Token.LBRACK ){
            poiMed++;
            dim++;
            if( getWordMed(poiMed).type == Token.RBRACK ){
                poiMed++;
                while( getWordMed(poiMed).type == Token.LBRACK ){
                    dim++;
                    poiMed++;

                    //TODO:定义函数参数时其中的表达式也不能写入文件.
                    IntermediateCode.writeInFile = false;
                    expsym = ExpressionMediate.ConstExp();
                    IntermediateCode.writeInFile = true;

                    if( getWordMed(poiMed).type == Token.RBRACK ){
                        poiMed++;
                    }
                }
            }
        }
        symmed = symbolTableMediateList[nowMediateDimension].addSymbol( ident );
        symmed.id = id;
        symmed.type = dim;
        symmed.dimension = nowMediateDimension;

        if( dim == 2 )
            symmed.dim2 = Integer.parseInt(expsym.value);

        String reg = TemporaryRegister.getFreeReg();
        symmed.reg = reg;

        if( dim == 0 ){
            IntermediateCode.writeLlvmIrWord("i32 " + reg, false);
        }
        else if( dim == 1 ){
            IntermediateCode.writeLlvmIrWord("i32* " + reg, false);
        }
        else if( dim == 2 ){
            IntermediateCode.writeLlvmIrWord("[" + expsym.value + " x i32]* " + reg, false);
        }

        //TODO 填充原函数的参数信息。
        func.paramList[ func.paramNum++ ] = symmed;
    }
}
