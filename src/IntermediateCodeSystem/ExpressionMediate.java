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
        String str;
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            ExpSymbol expSym = Exp();
            e.addExpSymbol( expSym );

            if( getWordMed(poiMed).type == Token.RPARENT ){
                poiMed++;
            }
        }

        else if( getWordMed(poiMed).type == Token.INTCON ){
            NumberMed( e );
        }

        else if( getWordMed(poiMed).type == Token.IDENFR ){

            lvalSym lvsym = LValMediate.analysis();
            SymbolMediate symmed = SymbolTableMediate.findSymbol( lvsym.token );

            /*--------------------------变量维度为0--------------------------*/
            if( lvsym.dim == 0 ){
                //TODO 对于普通变量而言，由于存的时候是以 i32* 的形式，因此取值时需要先将其 load 入i32类型。
                if( symmed.type == 0 ){
                    String reg = TemporaryRegister.getFreeReg();
                    str = reg + " = load i32, i32* " + symmed.reg;
                    IntermediateCode.writeLlvmIr( str, true );
                    e.addExpSymbol( reg, 1, false);
                }
                else if( symmed.safe ){
                    e.addExpSymbol(String.valueOf( symmed.value ), 1, true);
                }
                else{
                    e.addExpSymbol( symmed.reg, 1, false);
                }
            }
            /*--------------------------变量维度为1--------------------------*/
            else if(  lvsym.dim == 1 ){
                String reg = TemporaryRegister.getFreeReg();

                //TODO 根据原符号的维度进行判断当前为取地址还是取值。
                if( symmed.type == 1 )
                    str = reg + IntermediateCode.getPoiOneDim( symmed.reg, lvsym.poi );
                else
                    str = reg + IntermediateCode.getArrOneDim( symmed.reg, String.valueOf(symmed.dim2), lvsym.poi);
                IntermediateCode.writeLlvmIr( str, true);

                if( symmed.type == 1 ){
                    String address = reg;
                    reg = TemporaryRegister.getFreeReg();
                    str = reg + " = load i32, i32* " + address;
                    IntermediateCode.writeLlvmIr( str, true);
                }

                e.addExpSymbol( reg, 1, false);
            }
            /*--------------------------变量维度为2--------------------------*/
            else if(  lvsym.dim == 2 ){
                String reg = TemporaryRegister.getFreeReg();
                str = reg + IntermediateCode.getPoiTwoDim(
                        symmed.reg,
                        String.valueOf(symmed.dim2),
                        lvsym.poi1,
                        lvsym.poi2
                );
                IntermediateCode.writeLlvmIr( str, true);

                String address = reg;
                reg = TemporaryRegister.getFreeReg();
                str = reg + " = load i32, i32* " + address;
                IntermediateCode.writeLlvmIr( str, true);

                e.addExpSymbol( reg, 1, false);
            }
        }
    }

    public static void UnaryExp( ExpAnalyse e ) throws IOException {
        // UnaryExp → PrimaryExp
        // UnaryExp → Ident '(' [FuncRParams] ')'
        // UnaryExp → UnaryOp UnaryExp

        //TODO add func
        if( getWordMed(poiMed).type == Token.IDENFR && getWordMed(poiMed+1).type == Token.LPARENT ){

            String str = "";
            String reg = "";
            String func;
            func = IdentMediate.analysis();

            //TODO 获取当前函数的信息。
            FunctionMediate fun;
            fun = FunctionMediateTable.findSymbol( func );

            if( getWordMed(poiMed).type == Token.LPARENT ) {
                str += "(";
                poiMed++;
                while (getWordMed(poiMed).type != Token.RPARENT) {
                    str += FuncRParamsMediate.analysis( fun );
                }
                str += ")";
                poiMed++;
            }

            //TODO 结合当前函数不同类型进行处理。
            if( fun.retType == 0 ){
                str = "call void @" + func + str;
                IntermediateCode.writeLlvmIr( str, true);
            }
            else{
                reg = TemporaryRegister.getFreeReg();
                str = reg + " = call i32 @" + func + str;
                IntermediateCode.writeLlvmIr( str, true);
                e.addExpSymbol( reg, 1, false);
            }
        }

        else if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT ){
            String str = getWordMed(poiMed).token;
            UnaryOpMed();
            UnaryExp( e );
            e.addExpSymbol( str, 3, false);
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

            e.addExpSymbol( str, 0, false);
        }
    }

    public static ExpSymbol AddExp() throws IOException {
        String str;
        ExpSymbol expsym;
        ExpAnalyse e = new ExpAnalyse();

        MulExp( e );

        while( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU ){
            str = getWordMed(poiMed).token;
            poiMed++;
            MulExp(e);
            e.addExpSymbol( str, 0, false );
        }

        expsym = e.quaternion();

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

    public static ExpSymbol ConstExp() throws IOException{

        ExpSymbol expsym;
        expsym = AddExp();

        return expsym;
    }
}
