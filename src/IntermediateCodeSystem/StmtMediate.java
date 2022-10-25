package IntermediateCodeSystem;
import ErrorHandlingSystem.WithOutSemiconError;
import GrammaticalSystem.Expression;
import LexicalSystem.Token;
import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;
import static GrammaticalSystem.GrammaticalAnalysis.poi;
import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;


public class StmtMediate {

    // Stmt → LVal '=' Exp ';'
    // | [Exp] ';'
    // | Block
    // | 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
    // | 'while' '(' Cond ')' Stmt
    // | 'break' ';'
    // | 'continue' ';'
    // | 'return' [Exp] ';'
    // | LVal '=' 'getint''('')'';'
    // | 'printf''('FormatString{','Exp}')'';'

    public static int ioPoi = 0;
    public static int ioNowPoi = 0;
    public static ExpSymbol[] ioList = new ExpSymbol[10000];

    public static boolean lvalEqualsExpJudge() throws IOException {
        int poiMedInit = poiMed;

        if( getWordMed(poiMed).type == Token.IDENFR && getWordMed(poiMed+1).type != Token.LPARENT ){

            LValMediate.analysis();

            if( getWordMed(poiMed).type == Token.ASSIGN ){
                poiMed = poiMedInit;
                return true;
            }else{
                poiMed = poiMedInit;
                return false;
            }

        }else{
            return false;
        }
    }
    public static void analysis() throws IOException {


        if( getWordMed(poiMed).type == Token.IFTK ){
            // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            handWithIf();
        }
        else if( getWordMed(poiMed).type == Token.WHILETK ){
            // Stmt → 'while' '(' Cond ')' Stmt
            handWithWhile();
        }
        else if( getWordMed(poiMed).type == Token.BREAKTK ){
            // Stmt → 'break' ';'
            poiMed++;
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
            }
        }
        else if( getWordMed(poiMed).type == Token.CONTINUETK ){
            // Stmt → 'continue' ';'
            poiMed++;
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
            }
        }
        else if( getWordMed(poiMed).type == Token.RETURNTK ){
            // Stmt → 'return' [Exp] ';'
            poiMed++;
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
                String str = "ret void";
                IntermediateCode.writeLlvmIr( str, true );
            }
            else{
                ExpSymbol expsym = ExpressionMediate.Exp();
                String str = "ret i32 " + expsym.value;
                IntermediateCode.writeLlvmIr( str, true );

                if( getWord(poi).type == Token.SEMICN ) {
                    poi++;
                }
            }
        }
        else if( getWordMed(poiMed).type == Token.PRINTFTK ){
            // Stmt → 'printf''('FormatString{','Exp}')'';'
            handWithPrintf();
        }
        else if( getWordMed(poiMed).type == Token.LBRACE ){
            // Stmt → Block
            BlockMediate.analysis( true );
        }
        else if( getWordMed(poiMed).type == Token.SEMICN ){
            // Stmt → ;
            poiMed++;
        }
        else if( lvalEqualsExpJudge() ){
            // Stmt → LVal '=' Exp ';'
            // Stmt → LVal '=' 'getint''('')'';'
            lvalSym lvsym = LValMediate.analysis();
            poiMed++;

            if( getWordMed(poiMed).type == Token.GETINTTK ){
                handWithGetint( lvsym );
            }else{
                lvalEqualExp( lvsym );
            }
        }
        else{
            // Stmt → Exp ';'
            ExpressionMediate.Exp();
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
            }
        }
    }

    public static void handWithGetint( lvalSym lvsym ) throws IOException {
        // Stmt → LVal '=' 'getint''('')'';'

        SymbolMediate symmed = SymbolTableMediate.findSymbol( lvsym.token );

        if( lvsym.dim == 0 ){ // 变量维度为0。
            symmed.safe = false;
            symmed.value = 0;

            String reg = TemporaryRegister.getFreeReg();
            String str = reg + " = call i32 @getint()";
            IntermediateCode.writeLlvmIr( str, true);
            str = "store i32 " + reg + ", i32* " + symmed.reg;
            IntermediateCode.writeLlvmIr( str, true);
        }
        else if( lvsym.dim == 1 ){

            symmed.notSafe();

            String reg = TemporaryRegister.getFreeReg();
            String str = reg + " = call i32 @getint()";
            IntermediateCode.writeLlvmIr( str, true);

            String input = reg;
            // 保存输入的值。

            reg = TemporaryRegister.getFreeReg();
            //TODO 根据原符号的维度进行判断当前为取地址还是取值。
            if( symmed.type == 1 )
                str = reg + IntermediateCode.getPoiOneDim( symmed.reg, lvsym.poi2 );
            else
                str = reg + IntermediateCode.getArrOneDim( symmed.reg, String.valueOf(symmed.dim2), lvsym.poi2);

            IntermediateCode.writeLlvmIr( str, true);
            // 获取对应的数组变量地址。

            str = "store i32 " + input + ", i32* " + reg;
            IntermediateCode.writeLlvmIr( str, true);

        }
        else if( lvsym.dim == 2){
            symmed.notSafe();

            String reg = TemporaryRegister.getFreeReg();
            String str = reg + " = call i32 @getint()";
            IntermediateCode.writeLlvmIr( str, true);

            String input = reg;
            // 保存输入的值。

            reg = TemporaryRegister.getFreeReg();
            str = reg + IntermediateCode.getPoiTwoDim(
                    symmed.reg,
                    String.valueOf(symmed.dim2),
                    lvsym.poi1,
                    lvsym.poi2
                    );
            IntermediateCode.writeLlvmIr( str, true);
            // 获取对应的数组变量地址。

            str = "store i32 " + input + ", i32* " + reg;
            IntermediateCode.writeLlvmIr( str, true);
        }

        poiMed++;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            if( getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                if( getWordMed(poiMed).type == Token.SEMICN ){
                    poiMed++;
                }
            }
        }
    }

    public static void lvalEqualExp( lvalSym lvsym ) throws IOException {
        // Stmt → LVal '=' Exp ';'
        String str = "";
        ExpSymbol exp = ExpressionMediate.Exp();
        SymbolMediate symmed = SymbolTableMediate.findSymbol( lvsym.token );

        if( lvsym.dim == 0 ){ // 变量维度为0。
            if( exp.haveValue ){
                symmed.safe = true;
                symmed.value = Integer.parseInt( exp.value );
            }
            else{
                symmed.safe = false;
                symmed.value = 0;
            }
            str = "store i32 " + exp.value + ", i32* " + symmed.reg;
            IntermediateCode.writeLlvmIr( str, true );
        }
        else if( lvsym.dim == 1 ){

            String reg = TemporaryRegister.getFreeReg();

            //TODO 根据原符号的维度进行判断当前为取地址还是取值。
            if( symmed.type == 1 )
                str = reg + IntermediateCode.getPoiOneDim( symmed.reg, lvsym.poi2 );
            else
                str = reg + IntermediateCode.getArrOneDim( symmed.reg, String.valueOf(symmed.dim2), lvsym.poi2 );

            IntermediateCode.writeLlvmIr( str, true);

            str = "store i32 " + exp.value + ", i32* " + reg;
            IntermediateCode.writeLlvmIr( str, true);

        }
        else if( lvsym.dim == 2 ){
            String reg = TemporaryRegister.getFreeReg();
            str = reg + IntermediateCode.getPoiTwoDim(
                    symmed.reg,
                    String.valueOf(symmed.dim2),
                    lvsym.poi1,
                    lvsym.poi2
            );
            IntermediateCode.writeLlvmIr( str, true);

            str = "store i32 " + exp.value + ", i32* " + reg;
            IntermediateCode.writeLlvmIr( str, true);
        }
        if( getWordMed(poiMed).type == Token.SEMICN ){
            poiMed++;
        }
    }

    public static void handWithIf() throws IOException {
        // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        poiMed++;
        if(getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            CondMediate.analysis(); // Cond
            if(getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                analysis(); // Stmt
                if(getWordMed(poiMed).type == Token.ELSETK ){
                    poiMed++;
                    analysis(); // Stmt
                }
            }
        }
    }

    public static void handWithWhile() throws IOException {
        // Stmt → 'while' '(' Cond ')' Stmt
        poiMed++;
        if(getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            CondMediate.analysis(); // Cond
            if(getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                analysis(); // Stmt
            }
        }
    }

    public static void handWithPrintf() throws IOException{
        // Stmt → 'printf''('FormatString{','Exp}')'';'
        String str = "";
        poiMed++;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            if (getWordMed(poiMed).type == Token.STRCON ){

                str = getWordMed(poiMed).token;
                System.out.println( str );
                poiMed++;
                while( getWordMed(poiMed).type == Token.COMMA){
                    poiMed++;
                    ioList[ ioPoi++ ] = ExpressionMediate.Exp();
                }
                if (getWordMed(poiMed).type == Token.RPARENT ){
                    poiMed++;
                    if (getWordMed(poiMed).type == Token.SEMICN ){
                        poiMed++;
                    }
                }
            }
        }
        for( int i = 0; i < str.length(); i++ ){
            if( str.charAt(i) == '\"'){
                continue;
            }
            if( str.charAt(i) == '%' && i + 1 < str.length() && str.charAt(i+1) == 'd'){
                String ioStr = "call void @putint(i32 " + ioList[ioNowPoi++].value + ")";
                IntermediateCode.writeLlvmIr( ioStr, true);
                i ++;
            }
            else if( str.charAt(i) == '\\' && i + 1 < str.length() && str.charAt(i+1) == 'n'){
                String ioStr = "call void @putch(i32 " + 10 + ")";
                IntermediateCode.writeLlvmIr( ioStr, true);
                i ++;
            }
            else{
                String ioStr = "call void @putch(i32 " + (byte)str.charAt(i) + ")";
                IntermediateCode.writeLlvmIr( ioStr, true);
            }
        }
    }
}
