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
            }else{
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
            String str = "scanf" + " " + symmed.token;
//            IntermediateCode.writeIntermediateCode( str );
        }
        else{
            if( lvsym.haveValue ){ // 当前的poi是一个常量。
                int poi = Integer.parseInt(lvsym.poi);
                symmed.safeList[ poi ] = false;
                symmed.value = 0;
                String str = "scanf" + " " + lvsym.token + "[" + poi + "]";
//                IntermediateCode.writeIntermediateCode( str );
            }
            else{
                for( int i = 0; i < 10000; i++ ){
                    symmed.safeList[ i ] = false;
                    symmed.value = 0;
                }
                String str = "scanf" + " " + lvsym.token + "[" + lvsym.poi + "]";
//                IntermediateCode.writeIntermediateCode( str );
            }
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
            String str = symmed.token + " = " + exp.value;
            IntermediateCode.writeIntermediateCode( str );
        }
        else{
            if( lvsym.haveValue ){ // 当前的poi是一个常量。
                int poi = Integer.parseInt(lvsym.poi);
                if( exp.haveValue ){
                    symmed.safeList[ poi ] = true;
                    symmed.valueList[ poi ] = Integer.parseInt( exp.value );
                }
                else{
                    symmed.safeList[ poi ] = false;
                    symmed.value = 0;
                }
                String str = lvsym.token + "[" + poi + "]" + " = " + exp.value;
                IntermediateCode.writeIntermediateCode( str );
            }
            else{
                for( int i = 0; i < 10000; i++ ){
                    symmed.safeList[ i ] = false;
                    symmed.value = 0;
                }
                String str = lvsym.token + "[" + lvsym.poi + "]" + " = " + exp.value;
                IntermediateCode.writeIntermediateCode( str );
            }
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
        poiMed++;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            if (getWordMed(poiMed).type == Token.STRCON ){
                poiMed++;
                while( getWordMed(poiMed).type == Token.COMMA){
                    poiMed++;
                    ExpressionMediate.Exp();
                }
                if (getWordMed(poiMed).type == Token.RPARENT ){
                    poiMed++;
                    if (getWordMed(poiMed).type == Token.SEMICN ){
                        poiMed++;
                    }
                }
            }
        }
    }
}
