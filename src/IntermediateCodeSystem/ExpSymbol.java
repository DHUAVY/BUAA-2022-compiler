package IntermediateCodeSystem;

public class ExpSymbol {

    public int type; // 0 -> op, 1-> ident, 3 -> unary op
    public String value; // 当前的值、寄存器或变量等。
    public boolean haveValue; // 当前的取值是否为一个常量。

    public ExpSymbol( String value, int type, boolean haveValue ){
        this.type = type;
        this.value = value;
        this.haveValue = haveValue;
    }

    @Override
    public String toString(){
        String str = "{ type = " + this.type + ", token = " + this.value + ", haveValue = " + this.haveValue + " }";
        return str;
    }
}
