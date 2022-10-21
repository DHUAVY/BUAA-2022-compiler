package IntermediateCodeSystem;
import LexicalSystem.Token;
import java.io.IOException;

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
            LValMediate.analysis();
            poiMed++;

            if( getWordMed(poiMed).type == Token.GETINTTK ){
                handWithGetint();
            }else{
                lvalEqualExp();
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

    public static void handWithGetint() throws IOException {
        // Stmt → LVal '=' 'getint''('')'';'
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

    public static void lvalEqualExp() throws IOException {
        // Stmt → LVal '=' Exp ';'
        ExpressionMediate.Exp();
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
