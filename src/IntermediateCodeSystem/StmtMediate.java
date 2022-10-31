package IntermediateCodeSystem;
import ErrorHandlingSystem.WithOutSemiconError;
import GrammaticalSystem.Expression;
import LexicalSystem.Token;
import java.io.IOException;

import static IntermediateCodeSystem.ExpAnalyse.*;
import static IntermediateCodeSystem.LoopMediate.*;
import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;
import static IntermediateCodeSystem.StringMediate.*;
import IntermediateCodeSystem.StringHandle;


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

    public final static int IF = 0;
    public final static int WHILE = 1;

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

            loopTop--; // 当前的循环结束。
        }
        else if( getWordMed(poiMed).type == Token.BREAKTK ){
            // Stmt → 'break' ';'
            poiMed++;
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
            }
            String str = "br label " + loopList[loopTop-1].loopEnd;
            IntermediateCode.writeLlvmIr(str, true);
        }
        else if( getWordMed(poiMed).type == Token.CONTINUETK ){
            // Stmt → 'continue' ';'
            poiMed++;
            if( getWordMed(poiMed).type == Token.SEMICN ){
                poiMed++;
            }
            String str = "br label " + loopList[loopTop-1].loopHead;
            IntermediateCode.writeLlvmIr(str, true);
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

                if( getWordMed(poiMed).type == Token.SEMICN ) {
                    poiMed++;
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
        symmed.globalVarChange();

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

        // TODO 将全局变量的形式匹配如当前的函数。
        symmed.globalVarChange();

        if( lvsym.dim == 0 ){ // 变量维度为0。
            if( exp.haveValue && mode == varMode){
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
        String nextReg;
        poiMed++;
        if(getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            nextReg = CondMediate.analysis( StmtMediate.IF ); // Cond
            if(getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                LabelMediate.labelPrint();
                analysis(); // Stmt
                IntermediateCode.writeLlvmIr("br label "+nextReg, true);

                LabelMediate.labelPrint();
                if(getWordMed(poiMed).type == Token.ELSETK ){
                    poiMed++;
                    analysis(); // Stmt
                }
                IntermediateCode.writeLlvmIr("br label "+nextReg, true);
            }
        }
        LabelMediate.labelPrint();
    }

    public static void handWithWhile() throws IOException {
        // Stmt → 'while' '(' Cond ')' Stmt
        String nextReg;
        poiMed++;
        if(getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            nextReg = CondMediate.analysis( StmtMediate.WHILE ); // Cond
            if(getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
                LabelMediate.labelPrint();
                analysis(); // Stmt
                IntermediateCode.writeLlvmIr("br label "+ nextReg, true);
            }
        }
        LabelMediate.labelPrint();
    }

    public static void handWithPrintf() throws IOException{
        // Stmt → 'printf''('FormatString{','Exp}')'';'
        int strPoi = 0;
        String str = "";
        poiMed++;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            if (getWordMed(poiMed).type == Token.STRCON ){
                strPoi = poiMed;
                poiMed++;
                str = getWordMed(strPoi).token;
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
        //printIOString( strPoi );
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

    public static void printIOString( int poi ) throws IOException{
        StringHandle strHandle = stringLibrary.get( poi );
        for( int i = 0; i < strHandle.num; i++ ){
            int len = strHandle.lenthList[i];
            if( len == -1 ){
                String ioStr = "call void @putint(i32 " + ioList[ioNowPoi++].value + ")";
                IntermediateCode.writeLlvmIr( ioStr, true);
            }
            else{
                String reg = TemporaryRegister.getFreeReg();
                String ioStr = reg + " = getelementptr inbounds [" + len + " x i8], [" + len + " x i8]* " +
                        strHandle.contentList[i] + ", i32 0, i32 0";
                IntermediateCode.writeLlvmIr( ioStr, true);
                ioStr = "call void @putstr(i8* " + reg + ")";
                IntermediateCode.writeLlvmIr( ioStr, true);
            }
        }
    }
}
