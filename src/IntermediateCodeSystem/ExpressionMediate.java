package IntermediateCodeSystem;
import LexicalSystem.Token;

import java.io.IOException;

import static IntermediateCodeSystem.IntermediateCode.*;


public class ExpressionMediate {
    public static ExpAnalyse[] expStack = new ExpAnalyse[100000]; // 当前正在处理的表达式栈。
    public static int expStackTop = 0; // 栈内的元素数量。

    public static ExpSymbol Exp() throws IOException {

        ExpSymbol expsym;
        expsym = AddExp();

        return expsym;
    }

    public static void PrimaryExp( ExpAnalyse e ) throws IOException {
        // PrimaryExp → '(' Exp ')' | LVal | Number
        int ret = 0;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            ExpSymbol expSym = Exp();
//            System.out.println(expSym);
            e.addSymbol( expSym.token, expSym.type);

            if( getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
            }
        }
        else if( getWordMed(poiMed).type == Token.INTCON ){
            NumberMed( e );
        }
        else if( getWordMed(poiMed).type == Token.IDENFR ){
            LValMediate.analysis();
        }
    }

    public static void UnaryExp( ExpAnalyse e ) throws IOException {
        // UnaryExp → PrimaryExp
        // UnaryExp → Ident '(' [FuncRParams] ')'
        // UnaryExp → UnaryOp UnaryExp

        if( getWordMed(poiMed).type == Token.IDENFR && getWordMed(poiMed+1).type == Token.LPARENT ){

            IdentMediate.analysis();
            if( getWordMed(poiMed).type == Token.LPARENT ) {

                poiMed++;

                while (getWordMed(poiMed).type != Token.RPARENT) {
                    FuncRParamsMediate.analysis(); // 如果有参数，在下一阶段中查明参数的个数后检查。
                }
                poiMed++;
            }
        }
        else if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT ){
            UnaryOpMed();
            UnaryExp( e );
        }
        else{
            PrimaryExp( e );
        }
    }

    public static void MulExp(ExpAnalyse e) throws IOException {
        String str;
        UnaryExp( e );
        while( getWordMed(poiMed).type == Token.MULT || getWordMed(poiMed).type == Token.DIV || getWordMed(poiMed).type == Token.MOD ){
            str = getWordMed(poiMed).token;

            poiMed++;
            UnaryExp(e);

            e.addSymbol( str, 0);
        }
    }

    public static ExpSymbol AddExp() throws IOException {
        String str;
        ExpSymbol expsym;
        ExpAnalyse e = new ExpAnalyse();

        expStack[expStackTop] = e;
        expStackTop++;

        MulExp( e );

        while( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU ){
            str = getWordMed(poiMed).token;
            poiMed++;
            MulExp(e);
            e.addSymbol( str, 0 );
        }

        expStackTop--;
        expsym = expStack[expStackTop].quaternion();

        return expsym;
    }

    public static void RelExp( ExpAnalyse e ) throws IOException {
        AddExp();
        while( getWordMed(poiMed).type == Token.LSS || getWordMed(poiMed).type == Token.GRE ||
                getWordMed(poiMed).type == Token.GEQ || getWordMed(poiMed).type == Token.LEQ){
            poiMed++;
            AddExp();
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

        AddExp();

        expStackTop--;
        expStack[expStackTop].quaternion();

    }
}
