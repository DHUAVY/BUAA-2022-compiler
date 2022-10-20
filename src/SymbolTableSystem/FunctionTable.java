package SymbolTableSystem;

import LexicalSystem.Lexical;

import java.util.HashMap;

public class FunctionTable {

    public static HashMap<String, Function> directory = new HashMap<>();

    public static void addFunction(Lexical lex, int type ){
        Function newFunction = new Function(lex.id, lex.token, type);
        FunctionTable.directory.put( lex.token, newFunction );
    }

    @Override
    public String toString(){
        return "This is " + " FunctionTable!" + '\n' +
                "The HashMap is " + directory + '!' + '\n';
    }

}
