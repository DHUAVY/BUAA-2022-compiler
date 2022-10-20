package IntermediateCodeSystem;

import java.io.IOException;

public class ExpAnalyse {

    public int poi = 0;

    public ExpSymbol[] expTable = new ExpSymbol[10000];

    public void addSymbol( String token, int type ){
        ExpSymbol exp = new ExpSymbol(token, type);
        expTable[ poi ] = exp;
        poi ++;
        //System.out.println( exp );
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
                    token = TemporaryRegister.getFreeReg();
                    b = stack[--top];
                    a = stack[--top];
                    str =  token + " = " + a.token + " " + expTable[i].token + " " + b.token + "\n";

                    stack[top++] = new ExpSymbol(token, 1);
                    IntermediateCode.writeIntermediateCode( str );
                }
            }
            return stack[--top];
        }else{
            return expTable[poi];
        }

    }


}
