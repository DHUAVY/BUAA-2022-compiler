package GrammaticalSystem;

import LexicalSystem.Token;

import java.io.IOException;

import static GrammaticalSystem.GrammaticalAnalysis.*;
public class Decl {

    public static void analysis() throws IOException {
        // Decl → ConstDecl | VarDecl
        if( getWord(poi).type == Token.CONSTTK ) {
            ConstDecl.analysis();
        }else{
            VarDecl.analysis();
        }
    }

}
