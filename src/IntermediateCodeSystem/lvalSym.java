package IntermediateCodeSystem;

public class lvalSym {
    int dim;
    String token;
    String poi; // 对于数组而言，其总偏移量。
    boolean haveValue; // 其偏移量是否为一个常数。

    public lvalSym( String token, String poi, boolean haveValue, int dim ){
        this.dim = dim;
        this.poi = poi;
        this.token = token;
        this.haveValue = haveValue;
    }
}
