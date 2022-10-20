package LexicalSystem;

public class Lexical{

    public int id; // 单词的统一编号，即在wordList中的位置；与Symbol的统一id相同。
    public int type; // 单词的类型。
    public int lineNumber; // 单词所在的行号。
    public String token; // 单词对应的字符串。

    public Lexical( String token, int type, int lineNumber, int id ){
        this.id = id;
        this.type = type;
        this.token = token;
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "LexicalSystem.Lexical{" + '\n' +
                "id=" + id + ", " + '\n' +
                "type=" + type + ", " + '\n' +
                "token=" + token + ',' + '\n' +
                "lineNumber=" + lineNumber + ", " + '\n' +
                '}';
    }
}