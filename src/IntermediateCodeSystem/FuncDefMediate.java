package IntermediateCodeSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;

public class FuncDefMediate {

    public static String getFuncType( int type ){
        if( type == 0 )
            return "void";
        return "i32";
    }

    public static void analysis() throws IOException {
        // FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
        String type;
        String func;
        String str;
        int funcType = 0;
        SymbolMediate symmde;
        FunctionMediate function;

        funcType = FuncTypeMed();

        type = getFuncType( funcType ) ; // 函数类型。
        func = IdentMediate.analysis(); // 函数名称。

        symmde = IntermediateCode.symbolTableMediateList[nowMediateDimension].addSymbol( func );
        symmde.type = -1; // 函数。

        SymbolTableMediate.addDimension();

        //TODO 填充函数表
        function = FunctionMediateTable.addSymbol( func );
        function.retType = funcType;


        str = "define " + type + " @" + func;
        IntermediateCode.writeLlvmIrWord( str, false);

        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            IntermediateCode.writeLlvmIrWord( "(", false);

            if( getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                IntermediateCode.writeLlvmIr( "){", false );

                BlockMediate.analysis( false );
            }
            else{
                FuncFParamsMediate.analysis( function );
                if( getWordMed(poiMed).type == Token.RPARENT ){
                    poiMed++;
                    IntermediateCode.writeLlvmIr( "){", false );
                    BlockMediate.analysis( false );
                }
            }
        }
        str = "}";
        IntermediateCode.writeLlvmIr( str, false );
    }
}
