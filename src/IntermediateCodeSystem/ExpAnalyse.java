package IntermediateCodeSystem;

class ExpSymbol{
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

public class ExpAnalyse {

    public static int poi = 0;

    public static ExpSymbol[] expTable = new ExpSymbol[100000];

    public static void addSymbol(  String token, int type ){
        ExpSymbol exp = new ExpSymbol(token, type);
        expTable[ poi ] = exp;
        poi ++;
        System.out.println( exp );
    }

}
