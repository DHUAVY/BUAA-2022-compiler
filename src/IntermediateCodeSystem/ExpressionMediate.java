package IntermediateCodeSystem;
import LexicalSystem.Lexical;
import LexicalSystem.Token;
import SymbolTableSystem.FunctionTable;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.getWordMed;
import static IntermediateCodeSystem.IntermediateCode.poiMed;


public class ExpressionMediate {
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
        
        return ret;
    }

    public static int PrimaryExp( ExpAnalyse e ) throws IOException {
        // PrimaryExp → '(' Exp ')' | LVal | Number
        int ret = 0;
        if( getWordMed(poiMed).type == Token.LPARENT ){

            poiMed++;
            ret = Exp();

            ExpSymbol expSym = ExpAnalyse.expSymbolStack[--ExpAnalyse.expSymbolStackTop];
            e.addSymbol( expSym.token, expSym.type);

            if( getWordMed(poiMed).type == Token.RPARENT ){
                writeWord( getWordMed(poiMed) );
                poiMed++;
            }else{
                WithOutParenError.analyse( poiMed - 1);
            }

            writeGrammer("PrimaryExp");
        }
        else if( getWordMed(poiMed).type == Token.INTCON ){
            Number( e );
            writeGrammer("PrimaryExp");
            ret = 0; // 返回值为Number，因此一定为int类型。
        }
        else if( getWordMed(poiMed).type == Token.IDENFR ){
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
        Lexical lex;

        if( getWordMed(poiMed).type == Token.IDENFR && getWordMed(poiMed+1).type == Token.LPARENT ){
            lex = getWordMed(poiMed);


            Ident.ident();
            if( getWordMed(poiMed).type == Token.LPARENT ) {

                poiMed++;

                if( getWordMed(poiMed).type == Token.RPARENT ){
                    FuncParamNumError.analyse( lex, 0 ); // 如果没有参数，直接检查。
                }

                while( getWordMed(poiMed).type != Token.RPARENT ){

                    if( getWordMed(poiMed).type != Token.LPARENT &&
                            getWordMed(poiMed).type != Token.IDENFR &&
                            getWordMed(poiMed).type != Token.INTCON &&
                            getWordMed(poiMed).type != Token.PLUS &&
                            getWordMed(poiMed).type != Token.MINU)
                    {
                        break;
                    }

                    FuncRParams.Analysis( lex, flag ); // 如果有参数，在下一阶段中查明参数的个数后检查。
                }



                poiMed++;

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
        else if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT ){
            UnaryOp();
            UnaryExp( e );
            ret = 0; // 由于!仅出现在条件表达式中，因此此处可以推断直接为int类型。
        }
        else{
            ret = PrimaryExp( e );
        }

        return ret;
    }

    public static int MulExp(ExpAnalyse e) throws IOException {
        int ret;
        String str;
        ret = UnaryExp( e );
        while( getWordMed(poiMed).type == Token.MULT || getWordMed(poiMed).type == Token.DIV || getWordMed(poiMed).type == Token.MOD ){
            str = getWordMed(poiMed).token;

            poiMed++;
            UnaryExp(e);

            e.addSymbol( str, 0);

        }
        return ret;
    }

    public static int AddExp( ExpAnalyse e ) throws IOException {
        int ret;
        String str;
        ret = MulExp( e );
        while( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU ){
            str = getWordMed(poiMed).token;

            poiMed++;
            MulExp(e);

            e.addSymbol( str, 0 );

        }
        return ret;
    }

    public static void RelExp( ExpAnalyse e ) throws IOException {
        AddExp( e );
        while( getWordMed(poiMed).type == Token.LSS || getWordMed(poiMed).type == Token.GRE ||
                getWordMed(poiMed).type == Token.GEQ || getWordMed(poiMed).type == Token.LEQ){
            poiMed++;
            AddExp( e );
        }
    }

    public static void EqExp( ExpAnalyse e ) throws IOException {
        RelExp(e);
        while(getWordMed(poiMed).type == Token.EQL || getWordMed(poiMed).type == Token.NEQ ){
            poiMed++;
            RelExp(e);
        }
    }

    public static void LAndExp(ExpAnalyse e) throws IOException {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        EqExp(e);
        while(getWordMed(poiMed).type == Token.AND){
            poiMed++;
            EqExp(e);
        }
    }

    public static void LOrExp(ExpAnalyse e) throws IOException {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        LAndExp(e);
        while(getWordMed(poiMed).type == Token.OR){
            poiMed++;
            LAndExp(e);
        }
    }

    public static void ConstExp() throws IOException{

        ExpAnalyse e = new ExpAnalyse();
        expStack[expStackTop] = e;
        expStackTop++;

        AddExp( e );

        expStackTop--;
        expStack[expStackTop].quaternion();

    }
}
