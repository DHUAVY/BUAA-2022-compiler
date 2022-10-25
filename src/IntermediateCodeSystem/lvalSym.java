package IntermediateCodeSystem;

public class lvalSym {
    int dim;
    String token;
    //String poi; // 对于数组而言，其总偏移量。
    String poi1; // 对于数组而言，其第1维偏移量。
    String poi2; // 对于数组而言，其第2维偏移量。
    //boolean haveValue; // 其偏移量是否为一个常数。

    public lvalSym( String token, int dim, String poi1, String poi2 ){
        this.dim = dim;
        //this.poi = poi;
        this.token = token;
        //this.haveValue = haveValue;
        this.poi1 = poi1;
        this.poi2 = poi2;
    }
}
