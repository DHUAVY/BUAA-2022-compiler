package SymbolTableSystem;
import GrammaticalSystem.GrammaticalAnalysis;
import LexicalSystem.Lexical;

import java.util.HashMap;

public class SymbolTable {

    public int id; // 当前符号表的id。
    public int fatherId; // 当前符号表的父id。
    public HashMap<String, Symbol> directory = new HashMap<>();

    public static void init(){
        GrammaticalAnalysis.symbolTableList[0] = new SymbolTable( 0, -1 ); // 建立全局变量的符号表。
        GrammaticalAnalysis.dimensionNum = 1; // 当前具有的维度数目。
        GrammaticalAnalysis.nowDimension = 0; // 当前的维度。
    }

    public SymbolTable( int id, int fatherId ){
        this.id = id;
        this.fatherId = fatherId;
    }

    public void addSymbol( Lexical lex, int type, int con ){
        Symbol newSymbol = new Symbol(lex.id, lex.token, this.id, type, con);
        this.directory.put( lex.token, newSymbol );
    }

    @Override
    public String toString(){
        return  "This is " + id + " SymbolTable!" + '\n' +
                "The father id is " + fatherId + '!' + '\n' +
                "The HashMap is " + directory + '!' + '\n';
    }

}