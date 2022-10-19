package GrammaticalSystem;
import ErrorHandlingSystem.ErrorHandling;
import ErrorHandlingSystem.FuncParamNumError;
import ErrorHandlingSystem.NameUndefinitionError;
import ErrorHandlingSystem.WithOutParenError;
import IntermediateCodeSystem.ExpAnalyse;
import IntermediateCodeSystem.ExpSymbol;
import IntermediateCodeSystem.IntermediateCode;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import SymbolTableSystem.FunctionTable;

import java.io.IOException;
import static GrammaticalSystem.GrammaticalAnalysis.*;

public class Expression {

    public static ExpAnalyse[] expStack = new ExpAnalyse[100000]; // 当前正在处理的表达式栈。
    public static int expStackTop = 0; // 栈内的元素数量。

    public static int Exp() throws IOException {
        int ret;

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        ret = AddExp( e );

        expStackTop--;
        expStack[expStackTop].quaternion();

        writeGrammer("Exp");
        return ret;
    }

    public static int PrimaryExp( ExpAnalyse e ) throws IOException {
        // PrimaryExp → '(' Exp ')' | LVal | Number
        int ret = 0;
        if( getWord(poi).type == Token.LPARENT ){
            writeWord( getWord(poi) );
            poi++;
            ret = Exp();

            ExpSymbol expSym = ExpAnalyse.expSymbolStack[--ExpAnalyse.expSymbolStackTop];
            e.addSymbol( expSym.token, expSym.type);

            if( getWord(poi).type == Token.RPARENT ){
                writeWord( getWord(poi) );
                poi++;
            }else{
                WithOutParenError.analyse( poi - 1);
            }

            writeGrammer("PrimaryExp");
        }
        else if( getWord(poi).type == Token.INTCON ){
            Number( e );
            writeGrammer("PrimaryExp");
            ret = 0; // 返回值为Number，因此一定为int类型。
        }
        else if( getWord(poi).type == Token.IDENFR ){
            ret = LVal.Analysis( 0 );
            writeGrammer("PrimaryExp");
        }
        else{
            wrong();
        }
        return ret;
    }

    public static int UnaryExp( ExpAnalyse e ) throws IOException {
        // UnaryExp → PrimaryExp
        // UnaryExp → Ident '(' [FuncRParams] ')'
        // UnaryExp → UnaryOp UnaryExp
        int ret = 0;
        int withOutParen = 0;
        boolean flag;
        Lexical lex;

        if( getWord(poi).type == Token.IDENFR && getWord(poi+1).type == Token.LPARENT ){
            lex = getWord(poi);

            flag = NameUndefinitionError.analyse( lex ); // true -> error

            Ident.ident();
            if( getWord(poi).type == Token.LPARENT ) {
                writeWord(getWord(poi));
                poi++;

                if( getWord(poi).type == Token.RPARENT && !flag ){
                    FuncParamNumError.analyse( lex, 0 ); // 如果没有参数，直接检查。
                }

                while( getWord(poi).type != Token.RPARENT ){

                    if( getWord(poi).type != Token.LPARENT &&
                            getWord(poi).type != Token.IDENFR &&
                            getWord(poi).type != Token.INTCON &&
                            getWord(poi).type != Token.PLUS &&
                            getWord(poi).type != Token.MINU)
                    {
                        withOutParen = 1;
                        break;
                    }

                    FuncRParams.Analysis( lex, flag ); // 如果有参数，在下一阶段中查明参数的个数后检查。
                }


                if( withOutParen == 1 ){
                    WithOutParenError.analyse( poi - 1 );
                }else{
                    writeWord(getWord(poi)); // (
                    poi++;
                }

                if( !flag ){
                    if( FunctionTable.directory.get(lex.token).type == 1 ){ // 此处调用函数，如果为void则返回-1，反之则返回0。
                        ret = 0;
                    } else{
                        ret = -1;
                    }
                }else{
                    ret = -1;
                }
            }
        }
        else if( getWord(poi).type == Token.PLUS || getWord(poi).type == Token.MINU || getWord(poi).type == Token.NOT ){
            UnaryOp();
            UnaryExp( e );
            ret = 0; // 由于!仅出现在条件表达式中，因此此处可以推断直接为int类型。
        }
        else{
            ret = PrimaryExp( e );
        }

        writeGrammer("UnaryExp");
        return ret;
    }

    public static int MulExp(ExpAnalyse e) throws IOException {
        int ret;
        String str;
        ret = UnaryExp( e );
        writeGrammer("MulExp");
        while( getWord(poi).type == Token.MULT || getWord(poi).type == Token.DIV || getWord(poi).type == Token.MOD ){
            str = getWord(poi).token;

            writeWord(getWord(poi));
            poi++;
            UnaryExp(e);

            e.addSymbol( str, 0);

            writeGrammer("MulExp");
        }
        return ret;
    }

    public static int AddExp( ExpAnalyse e ) throws IOException {
        int ret;
        String str;
        ret = MulExp( e );
        writeGrammer("AddExp");
        while( getWord(poi).type == Token.PLUS || getWord(poi).type == Token.MINU ){
            str = getWord(poi).token;

            writeWord(getWord(poi));
            poi++;
            MulExp(e);

            e.addSymbol( str, 0 );

            writeGrammer("AddExp");
        }
        return ret;
    }

    public static void RelExp( ExpAnalyse e ) throws IOException {
        AddExp( e );
        writeGrammer("RelExp");
        while( getWord(poi).type == Token.LSS || getWord(poi).type == Token.GRE ||
                getWord(poi).type == Token.GEQ || getWord(poi).type == Token.LEQ){
            writeWord(getWord(poi));
            poi++;
            AddExp( e );
            writeGrammer("RelExp");
        }
    }

    public static void EqExp( ExpAnalyse e ) throws IOException {
        RelExp(e);
        writeGrammer("EqExp");
        while(getWord(poi).type == Token.EQL || getWord(poi).type == Token.NEQ ){
            writeWord(getWord(poi));
            poi++;
            RelExp(e);
            writeGrammer("EqExp");
        }
    }

    public static void LAndExp(ExpAnalyse e) throws IOException {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        EqExp(e);
        writeGrammer("LAndExp");
        while(getWord(poi).type == Token.AND){
            writeWord(getWord(poi));
            poi++;
            EqExp(e);
            writeGrammer("LAndExp");
        }
    }

    public static void LOrExp(ExpAnalyse e) throws IOException {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        LAndExp(e);
        writeGrammer("LOrExp");
        while(getWord(poi).type == Token.OR){
            writeWord(getWord(poi));
            poi++;
            LAndExp(e);
            writeGrammer("LOrExp");
        }
    }

    public static void ConstExp() throws IOException{

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        AddExp( e );

        expStackTop--;
        expStack[expStackTop].quaternion();

        writeGrammer("ConstExp");
    }
}
