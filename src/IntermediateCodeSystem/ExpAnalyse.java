package IntermediateCodeSystem;

import static IntermediateCodeSystem.ExpSymbol.*;
import java.io.IOException;
import java.util.Objects;

public class ExpAnalyse {

    public static int mode;
    public final static int funcMode = 0;
    public final static int varMode = 1;

    public int poi = 0;
    public ExpSymbol[] expTable = new ExpSymbol[10000];

    public void addExpSymbol( String token, int type, boolean haveValue){
        ExpSymbol exp = new ExpSymbol(token, type, haveValue);
        expTable[ poi ] = exp;
        poi ++;
    }

    public void addExpSymbol( ExpSymbol expsym ){
        expTable[ poi ] = expsym;
        poi ++;
    }

    public int calculate( String op, int a, int b ) throws IOException {
        if( op.equals("+") ){
            return a + b;
        }else if( op.equals("-") ){
            return a - b;
        }else if( op.equals("*") ){
            return a * b;
        }else if( op.equals("/") ){
            return a / b;
        }else if( op.equals("%") ){
            return a % b;
        }else if( op.equals("&&") ){
            return a & b;
        }
        else if( op.equals("||") ){
            return a | b;
        }
        else if( op.equals(">") ){
            if( a > b )
                return 1;
            else
                return 0;
        }
        else if( op.equals("<") ){
            if( a < b )
                return 1;
            else
                return 0;
        }
        else if( op.equals(">=") ){
            if( a >= b )
                return 1;
            else
                return 0;
        }
        else if( op.equals("<=") ){
            if( a <= b )
                return 1;
            else
                return 0;
        }
        else if( op.equals("==") ){
            if( a == b )
                return 1;
            else
                return 0;
        }
        else if( op.equals("!=") ){
            if( a != b )
                return 1;
            else
                return 0;
        }
        String str = "Wrong! op = " + op + " a = " + a + " b = " + b ;
        IntermediateCode.writeLlvmIr( str, true);
        return -1;
    }

    public void regCalculate( String op, String a, String b, String ans ) throws IOException {
        String str;
        str = ans + " = ";
        if( op.equals("+") ){
            str += "add i32 " + a + ", " + b;
        }else if( op.equals("-") ){
            str += "sub i32 " + a + ", " + b;
        }else if( op.equals("*") ){
            str += "mul i32 " + a + ", " + b;
        }else if( op.equals("/") ){
            str += "sdiv i32 " + a + ", " + b;
        }else if( op.equals("%")){
            str += "srem i32 " + a + ", " + b;
        }else if(  op.equals("&&") ){
            str += "and i1 " + a + ", " + b;
        }else if( op.equals("||") ){
            str += "or i1 " + a + ", " + b;
        }else if( op.equals(">") ){
            str += "icmp sgt i32 " + a + ", " + b;
        }else if( op.equals("<") ){
            str += "icmp slt i32 " + a + ", " + b;
        }else if( op.equals(">=") ){
            str += "icmp sge i32 " + a + ", " + b;
        }else if( op.equals("<=") ){
            str += "icmp sle i32 " + a + ", " + b;
        }else if( op.equals("==") ){
            str += "icmp eq i32 " + a + ", " + b;
        }else if( op.equals("!=") ){
            str += "icmp ne i32 " + a + ", " + b;
        }
        else{
            str = "Wrong! op = " + op + " a = " + a + " b = " + b ;
        }
        IntermediateCode.writeLlvmIr( str, true);
    }

    public boolean whenBackI1( String str ){
        return str.equals(">") || str.equals("<") || str.equals(">=") || str.equals("<=") ||
                str.equals("==") || str.equals("!=");
    }

    public boolean whenNeedI32( String str ){
        return !str.equals("&&") && !str.equals("||");
    }

    public ExpSymbol quaternion() throws IOException {

        ExpSymbol a;
        ExpSymbol b;
        String token = "";
        int top = 0;

        ExpSymbol[] stack = new ExpSymbol[100000];

        if( poi > 1 ){
            for( int i = 0; i <= poi-1; i++ ){
                if( expTable[i].type == 1 ){
                    stack[top++] = expTable[i];
                }
                else if( expTable[i].type == 0 ){
                    b = stack[--top];
                    a = stack[--top];
                    // 取得当前栈顶的两个操作数。

                    if( a.haveValue && b.haveValue && mode == varMode ){ // 当前是可以直接计算的常数。
                        String op = expTable[i].value;
                        int ret;
                        int aVal = Integer.parseInt( a.value );
                        int bVal = Integer.parseInt( b.value );

                        ret = calculate( op, aVal, bVal );
                        stack[top++] = new ExpSymbol( String.valueOf(ret), 1, true);
                        System.out.println( ret ); // 观测常数的计算结果。
                    }
                    else{
                        token = TemporaryRegister.getFreeReg();
                        if( whenNeedI32(expTable[i].value) ){
                            b.changeI1ToI32();
                            a.changeI1ToI32();
                        }

                        if( whenBackI1( expTable[i].value ))
                            stack[top++] = new ExpSymbol(token, 1, false, i1);
                        else
                            stack[top++] = new ExpSymbol(token, 1, false);

                        regCalculate(expTable[i].value, a.value, b.value, token );
                    }
                }
                else if(  expTable[i].type == 3 ){
                    ExpSymbol tran = stack[--top];
                    stack[top++] = new ExpSymbol("0", 1, true );
                    stack[top++] = tran;
                    expTable[i].type = 0;
                    if( expTable[i].value.equals("!") )
                        expTable[i].value = "==";
                    i = i - 1;
                }
            }
            return stack[--top];
        }
        else if( poi == 1){
            return expTable[poi-1];
        }
        else{
            return new ExpSymbol("", 1, false);
        }
    }
}
