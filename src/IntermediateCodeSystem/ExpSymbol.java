package IntermediateCodeSystem;

import java.io.IOException;

public class ExpSymbol {

    public final static int i32 = 0;
    public final static int i1 = 1;

    public int type; // 0 -> op, 1-> ident, 3 -> unary op
    public String value; // 当前的值、寄存器或变量等。
    public boolean haveValue; // 当前的取值是否为一个常量。
    public int llvmType = i32;

    public ExpSymbol( String value, int type, boolean haveValue){
        this.type = type;
        this.value = value;
        this.haveValue = haveValue;
    }

    public ExpSymbol( String value, int type, boolean haveValue, int llvmType ){
        this.type = type;
        this.value = value;
        this.haveValue = haveValue;
        this.llvmType = llvmType;
    }

    public void changeI1ToI32() throws IOException {
        if( this.llvmType == i1 ){
            String reg = TemporaryRegister.getFreeReg();
            String str = reg + " = zext i1 " + this.value + " to i32";
            IntermediateCode.writeLlvmIr( str, true );
            this.value = reg;
            this.llvmType = i32;
        }
    }

    @Override
    public String toString(){
        String str = "{ type = " + this.type + ", token = " + this.value + ", haveValue = " + this.haveValue + " }";
        return str;
    }
}
