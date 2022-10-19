package IntermediateCodeSystem;

public class ExpSymbol {
    public int type; // 0 -> op, 1-> ident
    public String token;

    public ExpSymbol( String token, int type ){
        this.type = type;
        this.token = token;
    }

    @Override
    public String toString(){
        String str = "{ type = " + this.type + ", " + "token = " + this.token + " }";
        return str;
    }
}
