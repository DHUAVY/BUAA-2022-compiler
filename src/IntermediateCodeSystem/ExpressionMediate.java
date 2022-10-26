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
        String str ="";
        if( getWordMed(poiMed).type == Token.LPARENT ){
            poiMed++;
            ExpSymbol expSym = Exp();
            e.addExpSymbol( expSym );

            if( getWordMed(poiMed).type == Token.RPARENT )
                poiMed++;
        }

        else if( getWordMed(poiMed).type == Token.INTCON ){
            NumberMed( e );
        }

        else if( getWordMed(poiMed).type == Token.IDENFR ){

            lvalSym lvsym = LValMediate.analysis();
            SymbolMediate symmed = SymbolTableMediate.findSymbol( lvsym.token );

            symmed.globalVarChange();
//            if( !symmed.initial && symmed.dimension == 0 && symmed.type != 0 ){
//                String newReg = TemporaryRegister.getFreeReg();
//                symmed.initial = true;
//                if( symmed.type == 1 ){
//                    IntermediateCode.changeOneDimensionPatten(
//                            newReg,
//                            symmed.reg,
//                            String.valueOf(symmed.dim2)
//                    );
//                }
//                else if( symmed.type == 2 ){
//                    IntermediateCode.changeTwoDimensionPatten(
//                            newReg,
//                            symmed.reg,
//                            String.valueOf(symmed.dim1),
//                            String.valueOf(symmed.dim2)
//                    );
//                }
//                symmed.reg = newReg;
//            }

            /*--------------------------变量维度为0--------------------------*/
            if( lvsym.dim == 0 ){
                //TODO 对于普通变量而言，由于存的时候是以 i32* 的形式，因此取值时需要先将其 load 入i32类型。
                if( symmed.type == 0 ){
                    String reg = TemporaryRegister.getFreeReg();
                    str = reg + " = load i32, i32* " + symmed.reg;
                    IntermediateCode.writeLlvmIr( str, true );
                    e.addExpSymbol( reg, 1, false);
                }
                else{
                    //TODO 由于已经初始化完成，这里直接将代表的数组送入即可。
                    e.addExpSymbol( symmed.reg, 1, false);
                }
            }
            /*--------------------------变量维度为1--------------------------*/
            else if(  lvsym.dim == 1 ){
                String reg = TemporaryRegister.getFreeReg();

                //TODO 根据原符号的维度进行判断当前为取地址还是取值。
                if( symmed.type == 1 )
                    str = reg + IntermediateCode.getPoiOneDim( symmed.reg, lvsym.poi2 );
                else if( symmed.type == 2 )
                    str = reg + IntermediateCode.getArrOneDim( symmed.reg, String.valueOf(symmed.dim2), lvsym.poi2 );

                IntermediateCode.writeLlvmIr( str, true);

                //TODO 对一维数组进行取值。
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
                //TODO 此时只可能为对当前的二维数组取值。
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

        //TODO UnaryExp → Ident '(' [FuncRParams] ')'
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
                while (getWordMed(poiMed).type != Token.RPARENT)
                    str += FuncRParamsMediate.analysis( fun );
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
        //TODO UnaryExp → UnaryOp UnaryExp
        else if( getWordMed(poiMed).type == Token.PLUS || getWordMed(poiMed).type == Token.MINU || getWordMed(poiMed).type == Token.NOT ){
            String str = getWordMed(poiMed).token;
            UnaryOpMed();
            UnaryExp( e );
            e.addExpSymbol( str, 3, false);
        }
        //TODO UnaryExp → PrimaryExp
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

    public static int RelExp( ExpAnalyse e ) throws IOException {

        //TODO 防止出现 if( a + b )这种情况。
        int signal = 1; // 0 -> if( a == 1 ...); 1 -> if( a ); 2 -> if( !a )
        ExpSymbol expsym = AddExp();
        e.addExpSymbol( expsym.value, 1, expsym.haveValue );

        while( getWordMed(poiMed).type == Token.LSS ||
                getWordMed(poiMed).type == Token.GRE ||
                getWordMed(poiMed).type == Token.GEQ ||
                getWordMed(poiMed).type == Token.LEQ
        ){
            signal = 0;
            String str = getWordMed(poiMed).token;
            poiMed++;
            expsym = AddExp();
            e.addExpSymbol( expsym.value, 1, expsym.haveValue );
            e.addExpSymbol( str, 0, false );
        }

        return signal;
    }

    public static void EqExp( ExpAnalyse e ) throws IOException {
        int signal;
        signal = RelExp(e);
        while(getWordMed(poiMed).type == Token.EQL || getWordMed(poiMed).type == Token.NEQ ){
            signal = 0;
            String str = getWordMed(poiMed).token;
            poiMed++;
            RelExp(e);
            e.addExpSymbol( str, 0, false );
        }
        if( signal == 1 ){
            //TODO 添加额外条件，if( a + b ) <==> if( (a + b) != 0 )
            e.addExpSymbol( "0", 1, true );
            e.addExpSymbol( "!=", 0, false );
        }
    }

    public static void LAndExp(ExpAnalyse e) throws IOException {
        // LAndExp → EqExp | LAndExp '&&' EqExp
        EqExp(e);
        while(getWordMed(poiMed).type == Token.AND){
            String str = getWordMed(poiMed).token;
            poiMed++;
            EqExp(e);
            e.addExpSymbol( str, 0, false );
        }
    }

    public static ExpSymbol LOrExp(ExpAnalyse e) throws IOException {
        // LOrExp → LAndExp | LOrExp '||' LAndExp
        LAndExp(e);
        while(getWordMed(poiMed).type == Token.OR){
            String str = getWordMed(poiMed).token;
            poiMed++;
            LAndExp(e);
            e.addExpSymbol( str, 0, false );
        }
        ExpSymbol expsym = e.quaternion();
        return expsym;
    }

    public static ExpSymbol ConstExp() throws IOException{

        ExpSymbol expsym;
        expsym = AddExp();

        return expsym;
    }
}
