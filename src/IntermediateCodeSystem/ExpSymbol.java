package IntermediateCodeSystem;

public class ExpSymbol {

    public int type; // 0 -> op, 1-> ident
    public String token;
    public boolean haveValue; // 当前的取值是否为一个常量。

    public ExpSymbol( String token, int type, boolean haveValue ){
        this.type = type;
        this.token = token;
        this.haveValue = haveValue;
    }

    @Override
    public String toString(){
        String str = "{ type = " + this.type + ", token = " + this.token + ", haveValue = " + this.haveValue + " }";
        return str;
    }
}
