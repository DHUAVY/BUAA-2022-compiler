package SymbolTableSystem;

public class Symbol {

    public int id; // 当前单词对应的id。
    public int dimension; // 当前单词所在的维度。
    public String token; // 当前单词所对应的字符串。
    public int type; // 0 -> a, 1 -> a[], 2 -> a[][], -1 -> func
    public int con; // 1 -> const, 0 -> var

    public Symbol( int id, String token, int dimension, int type, int con ){
        this.id = id;
        this.con = con;
        this.type = type;
        this.token = token;
        this.dimension = dimension;
    }

    @Override
    public String toString(){
        return "{" + '\n' +
                "id = " + id + '\n' +
                "dimension = " + dimension + '\n' +
                "token = " + token + '\n' +
                "type = " + type + '\n' + '}' + '\n';
    }

}
