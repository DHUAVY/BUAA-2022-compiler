package IntermediateCodeSystem;

import java.io.IOException;

public class ExpAnalyse {

    public int poi = 0;

    public ExpSymbol[] expTable = new ExpSymbol[10000];

    public void addExpSymbol( String token, int type, boolean haveValue ){
        ExpSymbol exp = new ExpSymbol(token, type, haveValue);
        expTable[ poi ] = exp;
        poi ++;
//        System.out.println( exp );
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
                }else{
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

                        System.out.println( ret );
                    }
                    else{
                        token = TemporaryRegister.getFreeReg();
                        str =  token + " = " + a.value + " " + expTable[i].value + " " + b.value;

                        stack[top++] = new ExpSymbol(token, 1, false);
                        IntermediateCode.writeIntermediateCode( str );
                    }
                }
            }
            return stack[--top];
        }else{
            return expTable[poi-1];
        }
    }
}
