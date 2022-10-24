package IntermediateCodeSystem;

import java.io.IOException;

public class FunctionMediate {
    public String token;
    public int retType; // 0 -> void, 1 -> int
    public int paramNum = 0;
    public SymbolMediate[] paramList = new SymbolMediate[10000];

    public void changeParam() throws IOException {
        for( int i = 0; i < paramNum; i++ ){
            SymbolMediate symmed = paramList[i];
            String reg = TemporaryRegister.getFreeReg();
            String str = reg + " = alloca i32";
            IntermediateCode.writeLlvmIr( str, true );

            str = "store i32 " + symmed.reg + ", i32* " + reg;
            IntermediateCode.writeLlvmIr( str, true );
            symmed.reg = reg;
        }
    }
}
