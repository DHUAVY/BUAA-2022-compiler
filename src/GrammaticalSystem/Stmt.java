package GrammaticalSystem;
import ErrorHandlingSystem.*;
import LexicalSystem.Token;
import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Stmt {

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
    public static int loopDepth = 0;

    public static boolean lvalEqualsExpJudge() throws IOException {
        int poiInit = poi;
        if( getWord(poi).type == Token.IDENFR && getWord(poi+1).type != Token.LPARENT ){

            notWrite = true;
            LVal.Analysis(0);
            notWrite = false;

            if( getWord(poi).type == Token.ASSIGN ){
                poi = poiInit;
                return true;
            }else{
                poi = poiInit;
                return false;
            }
        }else{
            return false;
        }

    }

    public static int analysis() throws IOException {

        int ret = 0;
        if( getWord(poi).type == Token.IFTK ){
            // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
            handWithIf();
        }
        else if( getWord(poi).type == Token.WHILETK ){
            // Stmt → 'while' '(' Cond ')' Stmt
            loopDepth ++;
            handWithWhile();
            loopDepth --;
        }
        else if( getWord(poi).type == Token.BREAKTK ){
            // Stmt → 'break' ';'
            if( loopDepth == 0 ){
                NotInLoopError.analyse(poi);
            }
            writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.SEMICN ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutSemiconError.analyse( poi - 1 );
            }
        }
        else if( getWord(poi).type == Token.CONTINUETK ){
            // Stmt → 'continue' ';'
            if( loopDepth == 0 ){
                NotInLoopError.analyse(poi);
            }
            writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.SEMICN ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutSemiconError.analyse( poi - 1 );
            }
        }
        else if( getWord(poi).type == Token.RETURNTK ){
            // Stmt → 'return' [Exp] ';'
            WithReturnError.lineNumber = getWord(poi).lineNumber; // return的行号

            writeWord( getWord(poi) );
            poi++;
            if( getWord(poi).type == Token.SEMICN ){ // return;
                writeWord( getWord(poi) );
                poi++;
            }else{
                Expression.Exp();
                if( getWord(poi).type == Token.SEMICN ){
                    writeWord( getWord(poi) );
                    poi++;
                }else{
                    WithOutSemiconError.analyse( poi - 1 );
                }
                ret = 1; // 具有return同时有返回值的情况下才会将值变为1。
            }
        }
        else if( getWord(poi).type == Token.PRINTFTK ){
            // Stmt → 'printf''('FormatString{','Exp}')'';'
            handWithPrintf();
        }
        else if( getWord(poi).type == Token.LBRACE ){
            // Stmt → Block
            Block.analysis( true );
        }
        else if( getWord(poi).type == Token.SEMICN ){
            // Stmt → ;
            writeWord( getWord(poi) );
            poi++;
        }
        else if( lvalEqualsExpJudge() ){
            // Stmt → LVal '=' Exp ';'
            // Stmt → LVal '=' 'getint''('')'';'

            LVal.Analysis(1);

            writeWord( getWord(poi) ); // =
            poi++;

            if( getWord(poi).type == Token.GETINTTK ){
                handWithGetint();
            }else{
                lvalEqualExp();
            }
        }
        else{
            // Stmt → Exp ';'
            Expression.Exp();
            if( getWord(poi).type == Token.SEMICN ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutSemiconError.analyse( poi - 1 );
            }
        }
        writeGrammer("Stmt");
        return ret;
    }

    public static void handWithGetint() throws IOException {
        // Stmt → LVal '=' 'getint''('')'';'
        writeWord( getWord(poi) );
        poi++;
        if( getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;

            if(  getWord(poi).type != Token.RPARENT ){
                WithOutParenError.analyse(poi-1);
            }else{
                writeWord( getWord(poi) );
                poi++;
            }

            if( getWord(poi).type == Token.SEMICN ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutSemiconError.analyse( poi - 1 );
            }
        }else{
            wrong();
        }
    }

    public static void lvalEqualExp() throws IOException {
        // Stmt → LVal '=' Exp ';'
        Expression.Exp();
        if( getWord(poi).type == Token.SEMICN ){
            writeWord( getWord(poi) );
            poi++;
        }else{
            WithOutSemiconError.analyse( poi - 1 );
        }
    }

    public static void handWithIf() throws IOException {
        // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        writeWord( getWord(poi) );
        poi++;
        if(getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;
            Cond.analysis(); // Cond

            if( getWord(poi).type != Token.RPARENT ){
                WithOutParenError.analyse( poi - 1 );
            }else{
                writeWord( getWord(poi) );
                poi++;
            }

            analysis(); // Stmt

            if(getWord(poi).type == Token.ELSETK ){
                writeWord( getWord(poi) );
                poi++;
                analysis(); // Stmt
            }
        }else{
            wrong();
        }
    }

    public static void handWithWhile() throws IOException {
        // Stmt → 'while' '(' Cond ')' Stmt
        writeWord( getWord(poi) );
        poi++;
        if(getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;
            Cond.analysis(); // Cond
            if(getWord(poi).type != Token.RPARENT ){
                WithOutParenError.analyse( poi - 1 );
            }else{
                writeWord( getWord(poi) );
                poi++;
            }
            analysis();
        }else{
            wrong();
        }
    }

    public static void handWithPrintf() throws IOException{
        // Stmt → 'printf''('FormatString{','Exp}')'';'
        int fc = 0, exp = 0; // 格式字符与表达式的数量。
        int lineNumber = 0;

        lineNumber = getWord(poi).lineNumber;

        writeWord( getWord(poi) ); // printf
        poi++;

        if( getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;
            if (getWord(poi).type == Token.STRCON ){

                IllegalSymbolError.analyse( getWord(poi) );
                fc = formatCharacterNum( getWord(poi).token );

                writeWord( getWord(poi) );
                poi++;

                while( getWord(poi).type == Token.COMMA){
                    writeWord( getWord(poi) );
                    poi++;
                    Expression.Exp();
                    exp ++;
                }

                if( fc != exp ){
                    FormatCharacterError.analyse(lineNumber);
                }

                if( getWord(poi).type != Token.RPARENT ){
                    WithOutParenError.analyse( poi-1);
                }else{
                    writeWord( getWord(poi) );
                    poi++;
                }

                if (getWord(poi).type == Token.SEMICN ){
                    writeWord( getWord(poi) );
                    poi++;
                }else{
                    WithOutSemiconError.analyse( poi - 1 );
                }
            }else{
                wrong();
            }
        }
    }

    public static int formatCharacterNum( String str ){
        int num = 0;
        for( int i = 0; i < str.length()-1; i++ ){
            if( str.charAt(i) == '%' && str.charAt(i+1) == 'd' ){
                num++;
            }
        }
        return num;
    }
}

