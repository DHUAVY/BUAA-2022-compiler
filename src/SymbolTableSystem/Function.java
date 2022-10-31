package SymbolTableSystem;

public class Function {

    public int id; // 当前单词对应的id。
    public String token; // 当前单词所对应的字符串。
    public int type; // 0 -> void, 1 -> int
    public int paramNum; // 当前函数的参数数量。
    public int[] paramList = new int[5000]; // 当前函数的参数表。 0->a, 1->a[], 2->a[][]

    public Function( int id, String token, int type ){
        this.id = id;
        this.type = type;
        this.token = token;
    }

}
