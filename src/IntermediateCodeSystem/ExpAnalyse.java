package IntermediateCodeSystem;

import java.io.IOException;

public class ExpAnalyse {

    public int poi = 0;
//    public boolean plusMinus; // 当前表达式最后是正是负。  true + : false -

    public ExpSymbol[] expTable = new ExpSymbol[10000];

    public void addExpSymbol( String token, int type, boolean haveValue){
        ExpSymbol exp = new ExpSymbol(token, type, haveValue);
        expTable[ poi ] = exp;
        poi ++;
    }

    public void addExpSymbol( ExpSymbol expsym ){
        expTable[ poi ] = expsym;
        poi ++;
//        System.out.println( expsym );
    }

    public int calculate( String op, int a, int b ){
        if( op.equals("+") ){
            return a + b;
        }else if( op.equals("-") ){
            return a - b;
        }else if( op.equals("*") ){
            return a * b;
        }else if( op.equals("/") ){
            return a / b;
        }else{
            return a % b;
        }
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
        }else{
            str += "srem i32 " + a + ", " + b;
        }
        IntermediateCode.writeLlvmIr( str, true);
    }

    public ExpSymbol quaternion() throws IOException {
        String str = "";
        String token = "";

        ExpSymbol a;
        ExpSymbol b;

        ExpSymbol[] stack = new ExpSymbol[100000];
        int top = 0;

        if( poi > 1 ){
            for( int i = 0; i <= poi-1; i++ ){
                if( expTable[i].type == 1 ){
                    stack[top++] = expTable[i];
                }
                else if( expTable[i].type == 0 ){
                    b = stack[--top];
                    a = stack[--top];
                    // 取得当前栈顶的两个操作数。

                    if( a.haveValue && b.haveValue ){ // 当前是可以直接计算的常数。
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

                        stack[top++] = new ExpSymbol(token, 1, false);
                        regCalculate(expTable[i].value, a.value, b.value, token );
                    }
                }
                else if(  expTable[i].type == 3 ){
                    ExpSymbol tran = stack[--top];
                    stack[top++] = new ExpSymbol("0", 1, true );
                    stack[top++] = tran;
                    expTable[i].type = 0;
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
